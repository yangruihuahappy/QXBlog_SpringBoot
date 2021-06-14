package com.tengshan.blog.dao;

import com.tengshan.blog.pojo.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    //根据标签下的博客数排
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
