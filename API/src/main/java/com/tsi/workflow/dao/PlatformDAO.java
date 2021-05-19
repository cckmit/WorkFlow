package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.Platform;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class PlatformDAO extends BaseDAO<Platform> {
    public Platform getPlatformByNickName(String pNickName) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("nickName", pNickName));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (Platform) lCriteria.uniqueResult();
    }
}
