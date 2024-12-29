package com.shopapp.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Setting;
import com.shopapp.common.enumm.SettingCategory;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String>{

	public List<Setting> findByCategory(SettingCategory category);
}
