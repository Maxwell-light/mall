package com.imooc.mall.service.impl;

import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.consts.MallConst.ROOT_PARENT_ID;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 耗时：http(请求微信api) > 磁盘 > 内存
     * mysql(内网+磁盘)
     *
     * @return
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {

        List<Category> categories = categoryMapper.selectAll();

        //查出parent_id=0(两种方法)
        //一、使用for循环
        /*for (Category category : categories) {
            if (category.getParentId().equals(ROOT_PARENT_ID)) {
                CategoryVo categoryVo = new CategoryVo();
                //Spring提供一个方法BeanUtils.copyProperties，将category对象里面的属性拷贝到categoryVo，不用挨个写。
                BeanUtils.copyProperties(category, categoryVo);
                categoryVoList.add(categoryVo);
            }
        }*/

        //二、使用lambda + stream
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::categoryToCategoryVo)
                //将查出的categoryVoList用sorted()方法进行排序，通过SortOrder进行排序，
                //默认为从小到大排序，使用reversed()方法进行翻转，变为从大到小进行排序。
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());

        //查询子目录
        findSubCategory(categoryVoList, categories);

        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }

    //查询下一级目录
    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categories) {
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();

            for (Category category : categories) {
                //如果查到内容，设置subCategory，继续往下查
                if (categoryVo.getId().equals(category.getParentId())) {
                    CategoryVo subCategoryVo = categoryToCategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }

                //将查出的subCategoryVoList用sort()方法进行排序，通过SortOrder进行排序，
                //默认为从小到大排序，使用reversed()方法进行翻转，变为从大到小进行排序。
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);

                //调用自身查是否还有下一级目录
                findSubCategory(subCategoryVoList, categories);

            }
        }
    }

    private CategoryVo categoryToCategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        //Spring提供一个方法BeanUtils.copyProperties，将category对象里面的属性拷贝到categoryVo，不用挨个写。
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }
}
