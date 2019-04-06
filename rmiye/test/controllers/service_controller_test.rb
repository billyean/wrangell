require 'test_helper'

class ServiceControllerTest < ActionDispatch::IntegrationTest
  test "should get add" do
    get service_add_url
    assert_response :success
  end

  test "should get update" do
    get service_update_url
    assert_response :success
  end

  test "should get delete" do
    get service_delete_url
    assert_response :success
  end

  test "should get get" do
    get service_get_url
    assert_response :success
  end

end
