package io.haibo.jdbc;

import io.haibo.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceJdbcDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Service> findAll() {
        return jdbcTemplate.query("SELECT * FROM service", new BeanPropertyRowMapper<Service>(Service.class));
    }

    public Service findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM service Where id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<Service>(Service.class));
    }
}
