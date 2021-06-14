package com.tengshan.blog.dao;

import com.tengshan.blog.pojo.Blog;
import com.tengshan.blog.pojo.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    //找到推荐状态下最新更新的
    @Query("select  b from  Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    //找到评论时间排序的博客
    @Query("select b from Blog b")
    List<Blog> findTopByCommentUpdateTime(Pageable pageable);

    //根据名称模糊查询博客信息
    @Query("select b from Blog b where b.title like CONCAT('%',:name,'%')")
    List<Blog> findByTitleLike(Pageable pageable, String name);

    //根据作者id查询博客信息
    @Query("select b from Blog b where b.user.id = ?1")
    List<Blog> findByAuthor(Long id);

    //根据作者昵称查询博客信息
    List<Blog> findByAuthor(String author);

    //根据标签查询博客信息
    @Query("select b from Blog b join b.tags t where t.id = ?1")
    List<Blog> findByTagIds(Long id);

    //根据分类id查询当前分类下的博客
//    @Query("select b from Blog b join b.type t where t.id = ?1")
    List<Blog> findByTypeId(Long id);

    //今日新增博客数量
    int countBlogsByCreateTimeAfter(Date createTime);

    //找到前7条博客信息
    @Query("select b from Blog b")
    List<Blog> findTopAll(Pageable pageable);

}
