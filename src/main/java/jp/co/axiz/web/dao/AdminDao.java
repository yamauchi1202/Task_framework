package jp.co.axiz.web.dao;

import jp.co.axiz.web.entity.Admin;

public interface AdminDao {

	public Admin findByIdAndPassword();
}
