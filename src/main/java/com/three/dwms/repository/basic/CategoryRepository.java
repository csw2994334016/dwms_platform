package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    int countByCategoryNameAndIdNot(String categoryName, Integer id);

    int countByCategoryName(String categoryName);
}
