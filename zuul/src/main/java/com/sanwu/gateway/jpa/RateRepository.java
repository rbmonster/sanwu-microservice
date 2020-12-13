package com.sanwu.gateway.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: RateRepository
 * @Author: sanwu
 * @Date: 2020/12/13 17:12
 */
@Repository
public interface RateRepository extends  CrudRepository<Rate,String>   {
}
