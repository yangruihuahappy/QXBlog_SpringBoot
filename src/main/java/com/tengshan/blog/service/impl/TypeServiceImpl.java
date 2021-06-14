package com.tengshan.blog.service.impl;

import com.tengshan.blog.dao.TypeRepository;
import com.tengshan.blog.pojo.Type;
import com.tengshan.blog.service.TypeService;
import com.tengshan.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public void saveType(Type type) {
        typeRepository.save(type);
    }

    @Override
    public Type findById(Long id) {
        return typeRepository.findById(id).orElse(null);
    }

    @Override
    public Type updateType(Long id, Type type) {
        Type type1 = typeRepository.findById(id).orElse(null);
        if(type1==null){
            System.out.println("获取更新对象失败");
        }else{
            BeanUtils.copyProperties(type,type1, MyBeanUtils.getNullPropertyNames(type));
        }
        return typeRepository.save(type1);
    }

    @Override
    public void deleteById(Long id) {
        typeRepository.deleteById(id);
    }
}
