package com.tengshan.blog.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "b_blog")
public class Blog {

    @Id //主键标识
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;
    private String title;
    private String content;
    private String firstImg;
    private Integer praiseNum;
    private boolean commentabled;
    private boolean recommend; //推荐
    private Integer countComment;//评论数

    @ManyToOne  //分类集
    private Type type;

    @ManyToMany(cascade = CascadeType.PERSIST)   //标签集 指定级联  给当前设置的实体操作另一个实体的权限
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne  //作者
    private User user;

    private String author;

    @Transient  //表示不会在数据库中生成这个字段  只存在于本实体中
    private String tagIds;

    public Blog() {
    }

    @Temporal(TemporalType.TIMESTAMP) //指定时间戳
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP) //指定时间戳
    private Date updateTime;
    @Temporal(TemporalType.TIMESTAMP) //指定时间戳
    private Date commentUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public boolean isCommentabled() {
        return commentabled;
    }

    public void setCommentabled(boolean commentabled) {
        this.commentabled = commentabled;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public Date getCommentUpdateTime() {
        return commentUpdateTime;
    }

    public void setCommentUpdateTime(Date commentUpdateTime) {
        this.commentUpdateTime = commentUpdateTime;
    }

    public Integer getCountComment() {
        return countComment;
    }

    public void setCountComment(Integer countComment) {
        this.countComment = countComment;
    }

    //对一对多的标签进行处理  如一个博客有id为12345的tag  初始化为 1,2,3,4,5
    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags){
        if(!tags.isEmpty()){
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for(Tag tag:tags){
                if(flag){
                    ids.append(",");
                }else{
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }else{
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstImg='" + firstImg + '\'' +
                ", praiseNum=" + praiseNum +
                ", commentabled=" + commentabled +
                ", recommend=" + recommend +
                ", countComment=" + countComment +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", author='" + author + '\'' +
                ", tagIds='" + tagIds + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
