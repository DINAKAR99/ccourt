package com.example.demo.listeners;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class ListenerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// public Map<String, Object> getDistrictMap() {
	// String sql = "SELECT HDM.DISTRICT_ID, HDM.DISTRICT_NAME FROM
	// tfiber_district_mst HDM ORDER BY HDM.DISTRICT_NAME"; //WHERE
	// HDM.IS_ACTIVE='Y'
	// return CommonDBUtils.getMap(sql, sessionFactory);
	// }
	// public Map<String, Object> getDistrictPoliceMap() {
	// String sql = "SELECT HDM.police_district_id, HDM.police_district_name FROM
	// master_district_police HDM ORDER BY HDM.police_district_name";
	// return CommonDBUtils.getMap(sql, sessionFactory);
	// }

	// public Map<String, Object> getPremisesLimits() {
	// String sql = "SELECT premises_limit_id, premises_limit_name FROM
	// public.master_premises_limits where is_active is true";
	// return CommonDBUtils.getMap(sql, sessionFactory);
	// }

}
