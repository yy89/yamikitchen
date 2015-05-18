package com.xiaobudian.yamikitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xiaobudian.yamikitchen.domain.http.HttpParams;

/**
 * Created by Liuminglu on 2015/5/15.
 */
public interface HttpClientRepository extends JpaRepository<HttpParams, Long> {

	@Query("from HttpParams where id = ?1")
	HttpParams getHttpParamsById(Long id);
	
}
