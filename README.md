# BaseFramework
BaseFramework框架是我对之前编程开发的一些总结，目的是以最快的方式完成项目开发，因此将一些常用的小工具，例如简易吐司、简易log等放在了基础类中，只需要将您项目中的Activity或Fragment继承本框架中的BaseActivity以及BaseFragment，即可使用。
除此之外BaseActivity还提供沉浸式适配，您可以查看Demo的源代码来了解更多。

<a href="https://github.com/kongzue/BaseFramework/">
<img src="https://img.shields.io/badge/BaseFramework-6.5.7.1-green.svg" alt="Kongzue BaseFramework">
</a> 
<a href="https://bintray.com/myzchh/maven/BaseFramework/6.5.7.1/link">
<img src="https://img.shields.io/badge/Maven-6.5.7.1-blue.svg" alt="Maven">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Homepage">
</a> 

Demo预览图如下：

![BaseFramework](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/BaseFramework.png)

试用版和使用方法 Demo 可以前往 https://fir.im/basefw 下载

![Kongzue's BaseFramework](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/download_baseframework.png)

## 使用前的约定与须知
- 更轻松！在 BaseActivity 中，约定关键词me代替Activity.this，因此您在编写代码时，在异步线程中可以轻松使用me关键字直接引用当前的父Activity。

- 请忘掉重写你的 onCreate 吧！在新版本的 BaseFramework 中，您可以直接在 class 上使用注解 @Layout(R.layout.xxx) ，剩下的事情我们会自动帮您完成！

- 在 BaseActivity 中，您还可以使用以下注解对沉浸式进行控制：
```
@DarkStatusBarTheme(true)           //开启顶部状态栏图标、文字暗色模式
@DarkNavigationBarTheme(true)       //开启底部导航栏按钮暗色模式
@NavigationBarBackgroundColor(a = 255,r = 255,g = 255,b = 255)      //设置底部导航栏背景颜色（a = 0,r = 0,g = 0,b = 0可透明）

//也可从代码中进行控制：
setDarkStatusBarTheme(true);            //开启顶部状态栏图标、文字暗色模式
setDarkNavigationBarTheme(true);        //开启底部导航栏按钮暗色模式
setNavigationBarBackgroundColor(Color.argb(255,255,255,255));       //设置底部导航栏背景颜色（a = 0,r = 0,g = 0,b = 0可透明）
```

- 规范化！无论是在 BaseActivity 还是 BaseFragment ，默认都有 initViews()、initDatas()、setEvents() 三个方法，他们分别代表加载组件、初始化数据、组件绑定事件三个步骤，因其执行顺序是固定的，且为了代码规范化，这三个方法必须重写，也建议将相关业务逻辑写在对应方法中，以方便维护和管理。

- 请注意，我们会尽可能快的支持最新的 Android Support库，当前BaseFragment已经集成“com.android.support:appcompat-v7:27.1.1”，如有冲突可能需要删除您的工程中的 Android Support 库。

## Maven仓库或Gradle的引用方式
Maven仓库：
```
<dependency>
  <groupId>com.kongzue.baseframework</groupId>
  <artifactId>baseframework</artifactId>
  <version>6.5.7.1</version>
  <type>pom</type>
</dependency>
```
Gradle：
在dependencies{}中添加引用：
```
implementation 'com.kongzue.baseframework:baseframework:6.5.7.1'
```

⚠从6.5.5版本起部分方法有较大变化，如有使用旧版本，请参阅<a href="#about">更新日志</a>

# 目录

· <a href="#1">BaseActivity功能</a>

···· <a href="#1-1">带自定义参数的跳转</a>

···· <a href="#1-2">更简单的跳转后返回数据</a>

···· <a href="#1-3">权限申请</a>

···· <a href="#1-4">BaseActivity提供的小工具</a>

···· <a href="#1-5">BaseActivity的生命周期</a>

· <a href="#2">BaseFragment功能</a>

· <a href="#3">BaseActivity提供的小工具</a>

· <a href="#4">Preferences</a>

· <a href="#5">AppManager</a>

· <a href="#6">BaseAdapter</a>

···· <a href="#6-1">JavaBean 适配方式</a>

···· <a href="#6-2">Map 适配方式</a>

···· <a href="#6-3">多种布局的绑定方式</a>

···· <a href="#6-4">数据刷新方法</a>

· <a href="#7">行为与日志监听</a>

## <a name="1">BaseActivity功能</a>


#### <a name="1-1">带自定义参数的跳转</a>
Android 默认的 Intent无法支持自定义类型参数的跳转，BaseActivity 通过自有的数据通道允许传输自定义类型的数据给要跳转到的另一个 BaseActivity：

跳转代码范例：
```
Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.img_bkg);
jump(JumpActivity.class, new JumpParameter()
        .put("参数1", "这是一段文字参数")
        .put("参数2", bmp)
);
```

接收数据代码范例：
```
String parameter1 = (String) getParameter().get("参数1");
if (!isNull(parameter1)) txtP1.setText("第一个参数读取到的值为：" + parameter1);
Bitmap parameter2 = (Bitmap) getParameter().get("参数2");
if (parameter2 != null) imgP2.setImageBitmap(parameter2);
```

#### <a name="1-2">更简单的跳转后返回数据</a>
以往我们需要通过重写实现 onActivityResult 来实现回传数据，但在 BaseActivity 中，你只需要一个监听器：

跳转代码范例：
```
jump(ResponseActivity.class, new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter == null) {
            toast("未返回任何数据");
        } else {
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});
```

亦可选用同时带参数+返回值的跳转：
```
jump(ResponseActivity.class,new JumpParameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
        , new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter==null){
            toast("未返回任何数据");
        }else{
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});
```

返回数据范例：
```
if ((boolean) getParameter().get("needResponse") == true) {
    setResponse(new Parameter().put("返回数据1", "测试成功"));
}

//或也可以使用：
if ((boolean) getParameter().get("needResponse") == true) {
    returnParameter(new Parameter().put("返回数据1", "测试成功"));
}
```

#### <a name="1-3">权限申请</a>
要进行权限申请也变得更加简单，只需要实现相应的回调 OnPermissionResponseListener 即可：
```
requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new OnPermissionResponseListener() {
    @Override
    public void onSuccess(String[] permissions) {
        toast("申请权限成功");
    }
    @Override
    public void onFail() {
        toast("申请权限失败");
    }
});
```

#### <a name="1-4">BaseActivity提供的小工具</a>
//简易吐司：

toast(Obj);

//简易Log打印日志（BaseFrameworkSettings.DEBUGMODE = false关闭，注意此开关是同时影响 BaseActivity 和 BaseFragment的）：

log(Obj);

//软键盘开关：

setIMMStatus(boolean show, EditText editText);

//dip与像素px转换：

dip2px(float dpValue);

//像素px与dip转换：

dip2px(float dpValue);

//属性动画：

moveAnimation(Object obj, String perference, float aimValue, long time, long delay);

//数据判空（适合网络返回值判断处理，即便为字符串“null”也为空）：

isNull(String);

//跳转动画（参数为您的动画资源id）：
jumpAnim(int enterAnim, int exitAnim)

#### <a name="1-5">BaseActivity的生命周期</a>

通常的，您可以通过重写 onCreate()、 onResume()、 onPause()、 onDestroy() 四个方法来监控 Activity 的生命周期，但因为每个方法为独立的方法函数，可能在较多代码时不容易寻找他们的存在。

在 BaseActivity 中，您可以通过 setLifeCircleListener(LifeCircleListener LifeCircleListener) 来直接添加一个生命周期监听器，其中就包含了上述的四个方法，进行统一管理：

```
setLifeCircleListener(new LifeCircleListener() {
    @Override
    public void onCreate() {

    }
    @Override
    public void onResume() {

    }
    @Override
    public void onPause() {

    }
    @Override
    public void onDestroy() {

    }
});
```

如果你要监控所有 BaseActivity 的生命周期，在 6.5.6 版本起新增了 setGlobalLifeCircleListener(GlobalLifeCircleListener globalLifeCircleListener) 用于对所有 BaseActivity 进行统一管理：
```
BaseActivity.setGlobalLifeCircleListener(new GlobalLifeCircleListener() {
    @Override
    public void onCreate(BaseActivity me, String className) {

    }
    @Override
    public void onResume(BaseActivity me, String className) {

    }
    @Override
    public void onPause(BaseActivity me, String className) {

    }
    @Override
    public void onDestroy(BaseActivity me, String className) {

    }
});
```
注意此方法为静态的，要使用它建议在 Application 中对它进行管理。

## <a name="2">BaseFragment功能</a>
BaseFragment 与普通的 Fragment 有什么区别？

首先，创建它变得异常的简单，你只需要在class上注解@Layout(你的布局资源文件id，例如R.layout.xxx)即可，剩下的事情BaseFragment会自动帮你完成。

除此之外，我们还支持了直接使用 findViewById ，而不需要额外的找到根布局 rootView，再 rootView.findViewById(...)，查看代码了解更多

BaseFragment 同样支持 BaseActivity 的一些小工具和组件，您可以轻松使用它们。

BaseFragment 也支持生命周期集中管理，您同样可以在 BaseFragment 中通过 setLifeCircleListener(LifeCircleListener LifeCircleListener) 监控 BaseFragment 的生命周期。

## <a name="3">Preferences</a>
Preferences是SharedPreferences的简易封装。

每次手写SharedPreferences过于繁琐，因此封装了一个简易的属性记录读取类。 通过对属性的常见数据类型进行封装，使属性读取写入更方便，同时提供一些属性管理方法。
```
//读取属性为String类型
//参数：context上下文索引，path路径，preferencesName属性名
getString(context, path, preferencesName)
//类似的，提供读取为Boolean的方法：
getBoolean(context, path, preferencesName)
//提供读取为Int的方法：
getInt(context, path, preferencesName)

//写入属性方法是统一的
//参数：context上下文索引，path路径，preferencesName属性名，value根据属性数据类型定义
set(context, path, preferencesName, ?)

//提供清除（清空）所有属性的方法
cleanAll();
```

## <a name="4">AppManager</a>
AppManager 是 BaseActivity 的管理工具类，原工具是由 @xiaohaibin(https://github.com/xiaohaibin) 所开发，经同意集成在 BaseFramework 中，此处略加修改更适合 BaseActivity 的管理工作。

提供如下方法：
```
killActivity(baseActivity)      //结束指定BaseActivity
killAllActivity()               //结束所有BaseActivity
AppExit()                       //退出App
```
其他方法，例如 pushActivity 添加Activity到堆栈，都是自动执行的，不需要手动调用。

## <a name="5">异步或同步</a>
有时我们需要等待一段时间执行事务，也有时我们需要从异步线程返回主线程进行 UI 等操作，这时往往需要在线程间进行切换进行操作，但偶尔也会因为执行过程中因 Activity 被关闭等问题出现空指针异常。

此时可以使用 BaseFramework 自带的异步同步方法轻松跳跃线程进行操作，此方法包含在 BaseActivity 和 BaseFragment 中：

1) 回到主线程操作：
```
runOnMain(new Runnable(){
    //此处可切换回主线程进行操作
});
```

2) 创建延迟在主线程执行操作：
```
runOnMainDelayed(new Runnable(){
    //此处可切换回主线程进行延迟操作
}, time);       //time 即延迟时间，毫秒单位
```

3) 创建延迟操作（用于代替 new Handler().postDelayed(Runnable, time)）：
```
runDelayed(new Runnable(){
    //此处可进行延迟操作（可能在异步线程）
}, time);       //time 即延迟时间，毫秒单位
```


## <a name="6">BaseAdapter</a>
注意，此处的 BaseAdapter 特指 com.kongzue.baseframework.BaseAdapter。

![Kongzue's BaseAdapter](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/download_baseadapter.png)

通常由系统提供的的 android.widget.BaseAdapter 和 android.widget.SimpleAdapter 虽然都能满足我们的日常所需，但面对各种自定义需求以及多种布局的需求时往往捉襟见肘，以至于我们不得不重写 Adapter 来满足需求。

但再重写过程中实际上是有很多重复性的代码，导致我们的项目臃肿不堪，从 v6.4.8 版本起，新增了 BaseAdapter 来实现各种自定义布局适配器的需求：

#### <a name="6-1">JavaBean 适配方式</a>

使用此方式需要先创建继承自 BaseAdapter.BaseDataBean 的 JavaBean 数据集合来封装数据，例如在我们 Demo 中的：
```
List<CustomDatas> datas = new ArrayList();
datas.add(new CustomDatas().setTitle("我是布局1"));
datas.add(new CustomDatas().setTitle("我是布局2"));
datas.add(new CustomDatas().setTitle("我是布局3"));
```
其中 CustomDatas 的具体代码为：
```
private class CustomDatas extends BaseAdapter.BaseDataBean {
    String title;

    @Override
    public CustomDatas setType(int type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CustomDatas setTitle(String title) {
        this.title = title;
        return this;
    }
}
```
它是一个典型的 JavaBean，其中所有属性请根据实际业务需求添加，并建议生成相应的 get、set 方法。

接下来创建适配器并绑定在相应组件上：
```
baseAdapter = new BaseAdapter(me, datas, R.layout.item_list_layout1, new SimpleAdapterSettings() {
    @Override
    public Object setViewHolder(View convertView) {
        ViewHolder1 viewHolder1 = new ViewHolder1();
        viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
        return viewHolder1;
    }

    @Override
    public void setData(Object viewHolder, BaseAdapter.BaseDataBean dataBean) {
        CustomDatas data = (CustomDatas) dataBean;
        ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
        viewHolder1.txtTitle.setText(data.getTitle());
    }
});
list.setAdapter(baseAdapter);
```
SimpleAdapterSettings 是一个适配器控制器的回调接口，在其中重写 setViewHolder 和 setData方法，其中 setViewHolder 需要您在此处根据父布局 convertView 创建布局管理组件 ViewHolder，并回传您的 ViewHolder。接下来会在 setData 中将 ViewHolder 和 相对应的数据 dataBean给出，请在此方法中对组件进行赋值和事件绑定。
注意在此方法中您可以将 dataBean 强转为您的 JavaBean 类，viewHolder 也可以强转为您的 ViewHolder。

#### <a name="6-2">Map 适配方式</a>

应对复杂多变的数据我们可能会选择使用 Map 来存储我们的需要展现的数据，BaseAdapter 亦支持此方式的数据，与上述方法类似，您可以轻松完成数据的绑定和组件的展现：
```
List<Map<String, Object>> datas = new ArrayList<>();
Map<String, Object> map = new HashMap<>();
map.put("title", "我是布局1");
datas.add(map);
map = new HashMap<>();
map.put("title", "我是布局2");
datas.add(map);
map = new HashMap<>();
map.put("title", "我是布局3");
datas.add(map);
baseAdapter = new BaseAdapter(me, datas, R.layout.item_list_layout1, new SimpleMapAdapterSettings() {
    @Override
    public Object setViewHolder(View convertView) {
        ViewHolder1 viewHolder1 = new ViewHolder1();
        viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
        return viewHolder1;
    }

    @Override
    public void setData(Object viewHolder, Map<String, Object> data) {
        ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
        viewHolder1.txtTitle.setText(data.get("title") + "");
    }
});
list.setAdapter(baseAdapter);
```

#### <a name="6-3">多种布局的绑定方式</a>

根据实际业务需求，我们可能需要在一个组件中展现多种布局，此时您首先需要对您的布局进行编号，从0开始，依次往后，并将他们添加为一个 Map 集合，其中键值对：id对应布局资源id（LayoutResId）：
```
Map<Integer, Integer> layoutResIdMap = new HashMap<>();
layoutResIdMap.put(0, R.layout.item_list_layout1);
layoutResIdMap.put(1, R.layout.item_list_layout2);
layoutResIdMap.put(2, R.layout.item_list_layout3);
```
接下来，您需要将数据存储为一个集合，此处展示的是 JavaBean 形式的存储方式，您亦可以使用 Map 作为数据的存储器，最后将它打包为一个 List即可：
```
List<CustomDatas> datas = new ArrayList();
datas.add(new CustomDatas().setTitle("我是布局1").setType(0));
datas.add(new CustomDatas().setTitle("我是布局2").setType(1));
datas.add(new CustomDatas().setTitle("我是布局3").setType(2));
```
需要注意的是，之前提到过，您的 JavaBean（CustomDatas）需要继承自 BaseAdapter.BaseDataBean，而在 BaseAdapter.BaseDataBean 中，我们默认实现了一个属性“type”，它是 int 整数型，用于存储与布局对应的编号 id。

如果您默认使用 Map 的方式存储数据，您需要手动 put("type", 对应布局编号id ) 以保证能够和布局资源相匹配。

最后，创建适配器和绑定展示组件：
```
baseAdapter = new BaseAdapter(me, datas, layoutResIdMap, new MultipleAdapterSettings() {
    @Override
    public Object setViewHolder(int type, View convertView) {
        switch (type) {
            case 0:
                ViewHolder1 viewHolder1 = new ViewHolder1();
                viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder1;
            case 1:
                ViewHolder2 viewHolder2 = new ViewHolder2();
                viewHolder2.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder2;
            case 2:
                ViewHolder3 viewHolder3 = new ViewHolder3();
                viewHolder3.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder3;
            default:
                return null;
        }
    }

    @Override
    public void setData(int type, Object viewHolder, BaseAdapter.BaseDataBean dataBean) {
        CustomDatas data = (CustomDatas) dataBean;
        switch (type) {
            case 0:
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
                viewHolder1.txtTitle.setText(data.getTitle());
                break;
            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2) viewHolder;
                viewHolder2.txtTitle.setText(data.getTitle());
                break;
            case 2:
                ViewHolder3 viewHolder3 = (ViewHolder3) viewHolder;
                viewHolder3.txtTitle.setText(data.getTitle());
                break;
        }
    }
});
list.setAdapter(baseAdapter);
```
从上述代码中可以看到，回调函数中出现了一个 type 的值，在这里您可以根据不同的值绑定不同的布局，设置不同的数据和事件。

以上就是关于 BaseAdapter 的简单介绍了。您还可以通过文档前半部分的二维码下载 Demo ，其中会为您展现关于 BaseAdapter 全部的绑定方式。

#### <a name="6-4">数据刷新方法</a>

从 v6.5.4 版本起，我们添加了 refreshDataChanged(...) 用于代替 notifyDataSetChanged() 刷新数据，该方法主要目的为解决 notifyDataSetChanged() 对于某些内容变化不敏感的问题。

使用方法为：
```
baseAdapter.refreshDataChanged(List<Map<String, Object>> newDatas);
//或
baseAdapter.refreshDataChanged(ArrayList<? extends BaseDataBean> newDatas);
```

## <a name="7">行为与日志监听</a>

![Kongzue's Beta Plan](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/betaplan_baseframework.jpg)

从 6.5.7 版本起新增了行为与日志监听功能，此功能默认是关闭的，此功能旨在帮助开发者进行良好的排错，包含Activity基本生命周期、使用log(...)语句输出的、使用toast(...)语句建立提示的、以及崩溃信息，在开启BETA_PLAN模式的情况下将按照App启动时间生成活动日志文件，以方便将App移交测试组进行测试。

此功能的优势在于：

1.可记录设备信息、系统版本信息、软件版本信息、设备ID等；

2.可根据需要重点记录一些日志信息：注意，此工具仅限记录各Activity启停状态、使用BaseActivity、BaseFragment的log、toast方法生成的信息以及崩溃信息）而不会记录其他方式生成的日Logcat信息；

3.可根据日志顺序按流程追溯用户操作步骤；

您可以前往此处查看<a href="BUGREPORT.md">日志文件内容样例</a>

#### 开启功能

开启所有日志保存功能，包含 Activity 基本生命周期、使用 log(...) 语句输出的、使用 toast(...) 输出的信息：
```
BaseFrameworkSettings.BETA_PLAN = true;
```

开启崩溃日志监控功能：
```
BaseFrameworkSettings.turnOnReadErrorInfoPermissions(context, new OnBugReportListener() {
    @Override
    public void onReporter(File file) {
        Log.i(">>>", "onReporter: "+file.getAbsolutePath());
    }
});
```
当发生崩溃时，会在下次 App 启动后，此监听器中返回发生崩溃的整个 App 运行周期的日志文件（含崩溃信息）

崩溃日志监控功能可以在不开启 BETA_PLAN 的情况下单独使用。

注：获取的日志文件为 .bfl 格式的文本文件，可通过任意文本编辑器打开。

#### 建议
建议在 OnBugReportListener 中接收到日志文件后，显示对话框提示用户是否愿意帮助改进App，并在用户同意后上传文件到您的服务器。


## 开源协议
```
Copyright BaseFramework

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## <a name="about">更新日志</a>：
v6.5.7.1:
- 修复无法引用的bug；

v6.5.7:
- 修复了 AppManager 中 killActivity(Class) 可能引发崩溃的bug；
- 修改，将 BaseActivity.DEBUGMODE 移动到了 BaseFrameworkSettings.DEBUGMODE，原 BaseActivity.DEBUGMODE 不再使用；
- 新增<a href="#7">行为与日志监听</a>功能；

v6.5.6.1:
- 修复 JumpParameter 空指针问题；

v6.5.6:
- BaseActivity 新增全局生命周期管理 GlobalLifeCircleListener；
- 回传数据方法 setResponse(...) 现新增更符合直觉的 returnParameter(...)；
- JumpParameter 可直接解析更多数据类型，例如 double、float、long、short 等；
- initDatas(JumpParameter parameter) 中的参数 parameter 不再需要非空校验了；

v6.5.5.3:
- 修复遗留问题，BaseFragment的dip2px和px2dip将无需context参数；
- 修复遗留问题，getDisplayWidth()函数名已被修改正确；

v6.5.5.2:
- 修复 lifeCircleListener.onCreate() 无效的问题；

v6.5.5.1:
- 警告：因命名冲突，6.5.5版本起，跳转回调参数 OnResponseListener 改名为 OnJumpResponseListener；
- 可以使用bigLog(...)打印更长的日志了；
- dip2px和px2dip不再需要context参数；
- 显示/关闭键盘的方法setIMMStatus(...)将被更名为showIME(...)，目前新旧方法都可以使用；
- 更新getNavbarHeight()方法，可以获取到更为准确的底栏高度；
- 新增getRootHeight()方法，可以获取准确真正的屏幕高度（含底栏和状态栏）；
- 新增BaseActivity注解 @FullScreen(true)可以直接使Activity全屏；
- 修正了 @NavigationBarBackgroundColor(a,r,g,b)的默认值；
- jump支持了共享元素，只需要在原参数末尾增加共享元素view，例如jump(Class<?> cls, View transitionView)；

详细的更新说明请阅读：《BaseFramework 6.5.5.1版本更新报告》：https://www.jianshu.com/p/9c2e0039aca1

v6.5.4:
- 增加 BaseActivity 与 BaseFragment 一键管理生命周期监听器，可在 BaseActivity 的子类中使用 setLifeCircleListener(LifeCircleListener);
- BaseAdapter 增加了 refreshDataChanged(...) 用于代替 notifyDataSetChanged() 刷新数据，新方法对于内容的变化也很敏感；

v6.5.3:
- 可以使用 runOnMain(Runnable) 来执行需要在主线程执行的事务，该方法与 runOnUiThread() 的不同点在于会自动判断当前 BaseActivity 是否处于存活状态，无须担心因此出现的空指针问题；
- 可以使用 runOnMainDelayed(Runnable, time) 来执行需要在主线程延迟执行的事务；
- 可以使用 runDelayed(Runnable, time) 来替代 new Handler().postDelayed(Runnable, time) 执行延迟事务；

v6.5.2:
- 修复 setIMMStatus(boolean, Edittext) 开关输入法方法中，Edittext 可能为 NULL 导致空指针的问题；
- 跳转到应用设置方法 startAppSettings() 不再是私有的，他现在可以公开调用；

v6.5.1:
- 修复 bug；

v6.5.0:
- 修复 BaseFragment 中日志 log 打印不受 BaseActivity.DEBUGMODE 控制的问题；

v6.5.0:
- 跳转参数 JumpParameter 新增 getBoolean、getInt 和 getString 三个基础方法，从此方法获取数据不需要判断是否为空（null）以及进行强转类型；

v6.4.9:
- 为避免与 BaseOkHttp 框架冲突修改 com.kongzue.baseframework.util.Parameter 类名为 com.kongzue.baseframework.util.JumpParameter;
- initDatas() 现已改为携带参数的 initDatas(JumpParameter paramer); 可以直接获取使用jump方法跳转时所携带的跳转参数，请注意非空判断；

v6.4.8:
- 新增BaseAdapter；

v6.4.7:
- 修复一些bug；

v6.4.6:
- 新增AppManager管理器；
- 新增DarkNavigationBarTheme、DarkStatusBarTheme、NavigationBarBackgroundColor注解；

v6.4.0:
- 集成Preferences（SharedPreferences的封装，仅使用简单的get、set方法即可）

v6.3.0:
- 直接使用注解的方式绑定布局资源（@Layout）
- 为BaseFragment增添支持新的jump(...)跳转方法；

v6.2.0:
- 支持新的jump(...)跳转方法；
- 更新BaseFragment；

v6.1.0:
- 合并 BaseActivity 与 BaseFragment 为 BaseFramework 总框架；
