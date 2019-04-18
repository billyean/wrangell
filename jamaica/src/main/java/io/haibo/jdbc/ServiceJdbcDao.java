package io.haibo.jdbc;

import io.haibo.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ServiceJdbcDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    class ServiceRowMapper implements RowMapper<Service> {

        @Override
        public Service mapRow(ResultSet resultSet, int i) throws SQLException {
            Service service = new Service();
            service.setId(resultSet.getInt("id"));
            service.setName(resultSet.getString("name"));
            service.setDescription("general");
            service.setTimeTypes(resultSet.getString("timeTypes"));
            service.setRate(resultSet.getDouble("rate"));
            service.setLimits(resultSet.getInt("limits"));
            return service;
        }
    }

    public List<Service> findAll() {
        //return jdbcTemplate.query("SELECT * FROM service", new BeanPropertyRowMapper<Service>(Service.class));
        return jdbcTemplate.query("SELECT * FROM service", new ServiceRowMapper());
    }

    public Service findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM service Where id = ?",
                new Object[]{id},
//                new BeanPropertyRowMapper<Service>(Service.class));
                new ServiceRowMapper());
    }

    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM service WHERE id = ?", new Object[]{id});
    }

    public int insert(Service service) {
        return jdbcTemplate.update("INSERT INTO service(id, name, description, timeTypes, rate, limits) VALUES(?, ?, ?, ?, ?, ?)",
                new Object[]{service.getId(), service.getName(), service.getDescription(),
                            service.getTimeTypes(), service.getRate(), service.getLimits()});
    }
}
