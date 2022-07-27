# kotlin 常用类基础库

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

    // 基本库 androidx
    implementation 'androidx.appcompat:appcompat:1.3.0-rc01'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'androidx.cardview:cardview:1.0.0@aar'
    implementation 'com.google.android.material:material:1.4.0@aar' 
    或
    implementation 'com.android.support:design:28.0.0'

    
    // Dex处理
    implementation 'androidx.multidex:multidex:2.0.1'
    // EventBus解耦
    implementation 'org.greenrobot:eventbus:3.2.0'
    // 日志工具类
    implementation 'com.apkfuns.logutils:library:1.7.5'
    // logger
    implementation 'com.orhanobut:logger:2.2.0'
    // AndroidUtilCode 强大易用的安卓工具类库
    implementation 'com.blankj:utilcode:1.13.10'

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
    // 联系人/RecyclerView实现顶部悬浮、字母排序、过滤搜索
    implementation 'com.github.nanchen2251:WaveSideBar:1.0.6'
    // 高仿微信通讯录、高仿美团选择城市界面
    implementation 'com.github.mcxtzhang:SuspensionIndexBar:V1.0.0'
    // 流式布局
    implementation 'com.google.android:flexbox:1.1.1'
    // 时间轴
    implementation 'com.vivian.widgets:TimeLineItemDecoration:1.5'


    // SmartRefreshLayout
    implementation 'com.scwang.smart:refresh-layout-kernel:2.0.3'      //核心必须依赖
    implementation 'com.scwang.smart:refresh-header-classics:2.0.3'    //经典刷新头
    implementation 'com.scwang.smart:refresh-footer-classics:2.0.3'    //经典加载
    // SmartRefreshLayoutHorizontal 2.x
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

    // 通过一行代码即可实现阴影效果
    implementation 'com.github.Liberuman:ShadowDrawable:0.1'
    // 自动识别图片颜色（识别图片中的颜色）
    implementation 'com.android.support:palette-v7:28.+'

    

    // BGA Zxing
    implementation 'cn.yipianfengye.android:zxing-library:2.2'


    // 动态显示圆形图像或圆形文字的AvatarImageView
    implementation 'cn.carbs.android:AvatarImageView:1.0.4'
    // 圆角图片
    implementation 'com.makeramen:roundedimageview:2.3.0'
    // compress image 压缩图片
    implementation 'com.zxy.android:tiny:0.1.0'
    // compress video 压缩视频
    implementation 'com.iceteck.silicompressorr:silicompressor:2.2.1'
    // Glide 加载图片
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    // 提供了局部加载长图，还提供缩放支持
    implementation 'com.davemorrissey.labs:subsampling-scale-image-view:3.10.0'
    // universal-image-loader
    implementation 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
    // 圆角、圆形ImageView，可绘制边框，圆形时可绘制内外两层边框，支持边框不覆盖图片，可绘制遮罩
    implementation 'com.github.SheHuan:NiceImageView:1.0.5'
    // 群聊组合头像Bitmap    
    implementation 'com.github.Othershe:CombineBitmap:1.0.5'

    // 不规则图形布局的使用(或WidgetCommon不规则布局的实现方式)
    implementation 'com.yxf:clippathlayout:1.0.+'


    // 解析.plist文件
    implementation 'com.googlecode.plist:dd-plist:1.21'
    // zip解压
    implementation 'com.leo618:zip:0.0.1'


    // 查看网络发文件
    implementation 'com.joanzapata.pdfview:android-pdfview:1.0.4@aar'
    // x5 tbs 
    implementation 'com.tencent.tbs:tbssdk:44085'
    // 安卓短信/彩信发送库
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
    implementation 'com.github.EnthuDai:SimpleChart-Kotlin:1.0.8'

    // LoadingDialog
    implementation 'com.github.ForgetAll:LoadingDialog:v1.1.2'
    // PopupWindow
    implementation 'com.github.zyyoona7:EasyPopup:1.1.2'
        for AndroidX:
    implementation 'com.labo.kaji:relativepopupwindow:0.4.1'
        for legacy Support Library:
    implementation 'com.labo.kaji:relativepopupwindow:0.3.1'


    // 多种样式的进度条 – ProgressView
    implementation 'com.white:progressview:latest.release@aar'

    // Android 炫酷的多重水波纹
    implementation 'com.scwang.wave:MultiWaveHeader:1.0.0'
    //androidx
    implementation 'com.scwang.wave:MultiWaveHeader:1.0.0-andx'



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
    implementation 'com.google.code.gson:gson:2.8.9'
    // retrofit 引入示例:
    implementation rootProject.ext.dependencies["converter-gson"]
    // Rxjava
    implementation "io.reactivex.rxjava2:rxjava:2.2.6"
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
    // 基于RxJava打造的下载工具,支持多线程下载和断点续传,使用Kotlin编写
    implementation 'zlc.season:rxdownload3:1.2.7'


    // 版本更新 java方式引用
    implementation 'com.qianwen:update-app:3.5.2'
    // 版本更新 kotlin方式引用
    implementation 'com.qianwen:update-app-kotlin:1.2.3'
    // 版本更新用的okhttp-utils
    // implementation 'com.qianwen:okhttp-utils:3.8.0'


    // aliyun
    implementation 'com.aliyun.dpa:oss-android-sdk:2.9.2'
    // 阿里推送
    implementation ('com.aliyun.ams:alicloud-android-push:3.5.0@aar'){
    // transitive true // exclude(module: 'alicloud-android-utdid')
    // }


    
    // 大图浏览
    implementation 'com.bm.photoview:library:1.4.1'
    // 拍照/选择图片
    implementation 'com.jph.takephoto:takephoto_library:4.0.3'
    // 选择图库
    implementation 'cn.bingoogolapple:bga-photopicker:1.2.8@aar'
    implementation 'cn.bingoogolapple:bga-baseadapter:1.2.9@aar'
    // 图片选择
    implementation 'com.zhihu.android:matisse:0.5.3-beta3'
    // 图片、视频、音频 选择
    implementation('com.github.LuckSiege.PictureSelector:picture_library:v2.2.3') {
    // exclude group: 'com.github.bumptech.glide' //通过排除依赖冲突 
    // }


    // easypermissions 动态获取权限 Android Support Library
    implementation 'pub.devrel:easypermissions:2.0.1'
    // For developers using AndroidX in their applications
    implementation 'pub.devrel:easypermissions:3.0.0'
    // 一行代码搞定Android 6.0动态权限授权、权限管理
    implementation 'com.github.dfqin:grantor:2.5'

    // 一种粗暴快速的 Android 全屏幕适配方案
    implementation 'com.bulong.rudeness:rudeness:latest.release@aar'


    // ViewPagerIndicator(indicator 取代 tabhost，实现网易顶部tab，新浪微博主页底部tab，引导页，
    // 无限轮播banner等效果，高度自定义tab和特效，LazyFragment )
    implementation 'com.shizhefei:ViewPagerIndicator:1.1.6'
    // PagerTab
    implementation 'com.astuetz:pagerslidingtabstrip:1.0.1'
    // 全新的TabLayout
    implementation 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'


    // Android TextView设置两端对齐，不区分中英文
    implementation 'me.codeboy.android:align-text-view:2.3.2'
    // calligraphy（高效加载字体包）
    implementation 'uk.co.chrisjenx:calligraphy:2.1.0'


    // LitePal for Android SQLite 数据库
    implementation 'org.litepal.guolindev:core:3.2.3'
