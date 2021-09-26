# kotlin 基础库

引入方式：

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

    dependencies {
         implementation 'com.github.R-Gang:Android-Common:latest.integration'
    }

常用的github依赖：

    // 基本库
    implementation 'androidx.appcompat:appcompat:1.3.0-rc01'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.google.android.material:material:1.1.0@aar'
    implementation 'androidx.cardview:cardview:1.0.0@aar'

    
    // Dex处理
    implementation 'androidx.multidex:multidex:2.0.1'
    //EventBus解耦
    implementation 'org.greenrobot:eventbus:3.2.0'
    //日志工具类
    implementation 'com.apkfuns.logutils:library:1.7.5'
    // logger
    implementation 'com.orhanobut:logger:2.1.1'


    // BRVAH（强大而灵活的RecyclerView Adapter）
    implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.36'
    // 万能适配器
    implementation 'com.zhy:base-adapter:3.0.3'
    // recyclerview
    implementation 'androidx.recyclerview:recyclerview:1.1.0@aar'
    // xrecyclerview
    implementation('com.jcodecraeer:xrecyclerview:1.6.0') {
        // exclude group: 'com.android.support', module: 'design'
        // exclude group: 'com.android.support'  //排除依赖冲突
    }
    // 带侧滑删除recyclerview
    implementation 'com.yanzhenjie:recyclerview-swipe:1.1.4'
    //联系人/RecyclerView实现顶部悬浮、字母排序、过滤搜索
    implementation 'com.github.nanchen2251:WaveSideBar:1.0.6'
    //流式布局
    implementation 'com.google.android:flexbox:1.1.1'


    // SmartRefreshLayout
    implementation 'com.scwang.smart:refresh-layout-kernel:2.0.3'      //核心必须依赖
    implementation 'com.scwang.smart:refresh-header-classics:2.0.3'    //经典刷新头
    implementation 'com.scwang.smart:refresh-footer-classics:2.0.3'    //经典加载
    //SmartRefreshLayoutHorizontal 2.x
    implementation 'com.scwang.smart:refresh-layout-horizontal:2.0.0'


    // 沉浸式状态栏
    implementation 'com.jaeger.statusbarutil:library:1.5.1'
    // Android沉浸式效果的实现—Sofia的使用
    implementation 'com.yanzhenjie:sofia:1.0.4'


    // 导航栏
    implementation 'me.majiajie:pager-bottom-tab-strip:2.2.5'


    // AppBar 简单的视图行为
    implementation 'com.zoonref:simple-view-behavior:1.0'
    // AppBar 状态栏布局
    implementation 'com.github.todou:appbarspring:1.0.8'

    
    // 动画大全：nineoldandroids
    implementation 'com.nineoldandroids:library:2.4.0'


    // 高斯模糊(不推荐)
    implementation 'com.github.mmin18:realtimeblurview:1.0.4'
    // 高斯模糊（推荐）
    implementation 'jp.wasabeef:blurry:4.0.0'
    

    // BGA Zxing
    implementation 'cn.yipianfengye.android:zxing-library:2.2'


    //动态显示圆形图像或圆形文字的AvatarImageView
    implementation 'cn.carbs.android:AvatarImageView:1.0.4'
    // 圆角图片
    implementation 'com.makeramen:roundedimageview:2.3.0'
    //compress image 压缩图片
    implementation 'com.zxy.android:tiny:0.1.0'
    //compress video 压缩视频
    implementation 'com.iceteck.silicompressorr:silicompressor:2.2.1'
    // Glide 加载图片
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    // 提供了局部加载长图，还提供缩放支持
    implementation 'com.davemorrissey.labs:subsampling-scale-image-view:3.10.0'
    // universal-image-loader
    implementation 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'

    
    //解析.plist文件
    implementation 'com.googlecode.plist:dd-plist:1.21'
    // zip解压
    implementation 'com.leo618:zip:0.0.1'


    // x5 tbs
    implementation 'com.klinkerapps:android-smsmms:5.1.0'


    // 视频播放器 完整版引入
    implementation 'com.shuyu:GSYVideoPlayer:4.1.1'
    implementation 'com.shuyu:gsyVideoPlayer-armv7a:4.1.2'
    // 视频播放控件
    implementation 'com.github.flztsj:JieCaoVideoPlayer:4.8.6'



    // banner 使用方式1
    implementation 'com.youth.banner:banner:1.4.10'
    // Banner 使用方式2
    implementation 'com.coldmoqiuli:banners:1.0.0'


    // 图表
    implementation 'org.xclcharts:lib:2.4'


    //LoadingDialog
    implementation 'com.github.ForgetAll:LoadingDialog:v1.1.2'
    //PopupWindow
    implementation 'com.github.zyyoona7:EasyPopup:1.1.2'

    //多种样式的进度条 – ProgressView
    implementation 'com.white:progressview:latest.release@aar'


    // 滚轮控件
    implementation 'com.wx.wheelview:wheelview:1.3.3'
    // 选择器1
    implementation 'com.contrarywind:Android-PickerView:4.1.9'
    // 选择器2
    implementation 'com.github.zyyoona7:pickerview:1.0.9'


    // okhttp
    implementation 'com.lzy.net:okhttputils:1.8.1' // 可以单独使用，不需要依赖下方的扩展包
    // 扩展了下载管理和上传管理，根据需要添加(支持断点下载，支持突然断网,强杀进程后,断点依然有效)
    implementation 'com.lzy.net:okhttpserver:1.0.3'
    // okgo
    implementation 'com.lzy.net:okgo:3.0.4'
    // Gson
    implementation 'com.google.code.gson:gson:2.8.5'
    // retrofit 引入示例:
    // implementation rootProject.ext.dependencies["converter-gson"]
    //Rxjava
    // implementation "io.reactivex.rxjava2:rxjava:2.2.6"
    // implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
    // 基于RxJava打造的下载工具,支持多线程下载和断点续传,使用Kotlin编写
    implementation 'zlc.season:rxdownload3:1.2.7'


    // 版本更新
    implementation 'com.qianwen:update-app:3.5.2'
    // 版本更新用的okhttp-utils,okgo
    implementation 'com.qianwen:okhttp-utils:3.8.0'


    //aliyun
    implementation 'com.aliyun.dpa:oss-android-sdk:2.9.2'
    // 阿里推送
    implementation('com.aliyun.ams:alicloud-android-push:3.1.6@aar') {
    // transitive true // exclude(module: 'alicloud-android-utdid')
    // }


    //大图浏览
    implementation 'com.bm.photoview:library:1.4.1'
    // 拍照/选择图片
    implementation 'com.jph.takephoto:takephoto_library:4.0.3'
    //选择图库
    implementation 'cn.bingoogolapple:bga-photopicker:1.2.8@aar'
    implementation 'cn.bingoogolapple:bga-baseadapter:1.2.9@aar'
    // mediapicker  这个库picture_library:v1.4.1 包含了上面的库
    implementation('com.github.LuckSiege.PictureSelector:picture_library:v2.2.3') {
    // exclude group: 'com.github.bumptech.glide' //通过排除依赖冲突 
    // }


    // easypermissions 动态获取权限
    implementation 'pub.devrel:easypermissions:1.2.0'


    // 一种粗暴快速的 Android 全屏幕适配方案
    implementation 'com.bulong.rudeness:rudeness:latest.release@aar'


    // ViewPagerIndicator(indicator 取代 tabhost，实现网易顶部tab，新浪微博主页底部tab，引导页，
    // 无限轮播banner等效果，高度自定义tab和特效，LazyFragment )
    implementation 'com.shizhefei:ViewPagerIndicator:1.1.6'
    //PagerTab
    implementation 'com.astuetz:pagerslidingtabstrip:1.0.1'
    // 全新的TabLayout
    implementation 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'


    // Android TextView设置两端对齐，不区分中英文
    implementation 'me.codeboy.android:align-text-view:2.3.2'
    // calligraphy（高效加载字体包）`
    implementation 'uk.co.chrisjenx:calligraphy:2.1.0'
