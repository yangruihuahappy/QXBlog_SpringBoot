package com.tengshan.blog.service.impl;

import com.tengshan.blog.dao.TagRepository;
import com.tengshan.blog.pojo.Tag;
import com.tengshan.blog.service.TagService;
import com.tengshan.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    @Override
    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Tag findById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag tag1 = tagRepository.findById(id).orElse(null);
        if(tag1==null){
            System.out.println("获取更新对象失败");
        }else{
            BeanUtils.copyProperties(tag,tag1, MyBeanUtils.getNullPropertyNames(tag));

        }
        return tagRepository.save(tag1);
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort =Sort.by(Sort.Direction.DESC,"blogList.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    //将接收到的ids字符串转成int类型tagid集合
    private List<Long> convertToList(String ids){
        System.out.println("service接收的ids为："+ ids);
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids!=null){
            String[] idArray = ids.split(",");
            for(int i=0;i<idArray.length;i++){
                list.add(new Long(idArray[i]));
            }
        }
        System.out.println("service处理完成后的id list为："+list);
        return list;
    }
}
