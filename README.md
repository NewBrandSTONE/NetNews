#模仿网易新闻

##基础信息介绍

Android版本：4.4.2

IDE：Android Studio 2.2.0

使用到的第三方库：

* [okhttp](https://github.com/square/okhttp)
* [gson](https://github.com/google/gson)
* [universalimageloader](https://github.com/nostra13/Android-Universal-Image-Loader)
* [photoview](https://github.com/chrisbanes/PhotoView)

##效果预览
[效果](http://ofiamtjor.bkt.clouddn.com/Untitled2.gif)

![效果.gif](http://ofiamtjor.bkt.clouddn.com/Untitled2.gif)

##功能列表

###新闻启动广告页
- [x] 沉浸式
- [x] 自定义倒数计时控件
- [x] webview显示html内容  	

###新闻首页
- [x] Fragment+tabhost构建分类标签
- [x] 首页Handler的优化（使用弱引用）
- [x] 滑动到底部请求更多新闻数据
- [ ] 上拉刷新新闻
- [ ] 轮播图
	- [x] 左右滑动
	- [ ] 自动滚动 

###新闻详情页
- [x] js代码调用java代码显示图片
- [x] 使用photoview展示新闻详情页中的图片内容
- [ ] 侧滑返回(侧滑关闭activity)
- [ ] 分享
- [ ] 查看评论 
