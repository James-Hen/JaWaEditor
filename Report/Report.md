# Java 语言程序设计——编程作业报告

| 班级            | 姓名   | 学号       | 同组人        |
| --------------- | ------ | ---------- | ------------- |
| 计算机试验班 91 | 张骏扬 | 2173314309 | 李辰洋 李禹陪 |

## 项目介绍

本项目是课程 *JAVA语言程序设计COMP561805[01]* 的结课设计大作业，选择了作业要求中的 ***图文混排编辑器*** 进行实现，原基本要求如下：

> - 用图形用户界面实现；
> - 能实现文字编辑、保存、另存为、查找替换等功能；
> - 支持网络储存与读取（提示：需要编写网络服务器程序配合），在网速较慢时不能影响当前的编辑功能（提示：开辟网络传输的专门线程）；
> - 能够实现一定的界面功能：改变文字的字体类型、显示颜色及显示大小；
> - 支持图片的插入、删除及大小编辑。



## 合作与分工

本项目由三位同学合作完成，使用 Git 工具进行同步开法与版本管理，各自完成的工作如下：

| 开发者 | 主要工作               |
| ------ | ---------------------- |
| 张骏扬 | UI/UX 与编辑器功能设计 |
| 李辰洋 | 网络通信与版本功能     |
| 李禹陪 | 本地保存器与实验报告   |



## 项目实现

### 项目文件结构

下面列出了本项目实现所建立的文件（类）结构。

```plain
.
└── xjtujavacourse
    ├── client							：客户端目录
    │   ├── ColorComboBox.java				：颜色选择组件
    │   ├── DebugFrame.java					：HTML 调试框
    │   ├── EditStack.java					：版本编辑栈
    │   ├── EditorFrame.java				：主界面
    │   ├── FindAndReplacePanel.java		：查找替换界面
    │   ├── LocalSaver.java					：本地保存器
    │   ├── Main.java						：程序入口
    │   ├── RemoteSaver.java				：远程保存器
    │   ├── Saver.java						：保存器基类
    │   ├── SizeComboBox.java				：字号选择组件
    │   └── TextStyleCheckbox.java			：字体样式组件
    ├── common							：共用目录
    │   ├── Base64Serializer.java			：实现图像编码保存
    │   └── JaWaDocument.java				：被编辑文件类
    └── server							：服务端目录
        └── ServerMain.java					：服务端程序
```

### 基本编辑器逻辑

本项目（下称 ***JaWa Editor***）采用 `Swing` 类库开发 GUI 组件，选用  `HTML` 作为富文本编辑编码表示。使用的核心类/库有：

- `JTextPanel` 与 `HTMLEditorKit`：两者结合提供主要的编辑与渲染框架，其中 `JTextPanel` 作为 `Swing` 组件提供用户交互接口，`HTMLEditorKit` 提供 HTML 文档渲染与样式更改动作；
- `HttpServer` 类：作为服务端开发框架；
- `java.io` 与 `java.net` 包：提供了网络 Socket 编程与序列化 I/O 工具，用于实现文件保存和管理。

项目分为两个部分：客户端和服务端。客户端可以独立运作，但客户端的远程保存、读取功能需要服务端正在运行并提供端口。

***JaWa Editor*** 所支持编辑的文件格式称为 *JaWa 文档*（`.JaWa` 文件），其为 HTML 文档的封装。

客户端包括 `client/` 目录中所有类以及 `common/` 目录中所有类。

服务端包括 `server/` 目录中所有类以及 `common/` 目录中所有类。

### UI/UX 设计

#### 编辑功能

<img src=".\GeneralUIOverview.png" alt="General UI Overview" style="zoom: 67%;" />

如图为 ***JaWa Editor*** 的主界面，标题栏下拉菜单包括文件菜单、编辑菜单以及帮助菜单，分别提供文件保存、加载；编辑选项和其他功能。标题栏显示了目前打开的文档名。

主界面使用 `BorderLayout`，其中 `NORTH` 部分是文字选项部分，提供粗体、斜体、下划线、颜色、字号选项。用户可以选中文本设定这些选项，在移动光标时，这些选项的选中/值会根据光标位置（下一个输入的字符属性）改变。

文字选项部分的标签颜色、样式提示是通过 `Swing` 自带支持 HTML 属性实现的，比如颜色选择组件（继承了 `JComboBox<String>`）的文本：

```java
String showStr
    = "<html><font color=\"" + colorToHexValue(actColor) + "\">" + str + "<html>";
addItem(showStr);
```

`CENTER` 部分是 `JTextPanel` 组件，用于用户输入和输入文档渲染。

字体样式的改变主要由 `StyledEditorKit` 中的样式动作实现：

```java
new StyledEditorKit.BoldAction();								// 粗体
new StyledEditorKit.ItalicAction();								// 斜体
new StyledEditorKit.UnderlineAction();							// 下划线
new StyledEditorKit.ForegroundAction("BLACK", Color.BLACK);		// 前景色
new StyledEditorKit.FontSizeAction(String.valueOf(i), i);		// 字体大小
```

而移动光标时的属性跟随是由鼠标监听器 `MouseListener` 调用以下函数实现的：

```java
private void refreshStyleToolStatus() {
    AttributeSet attributeSet = textKit.getInputAttributes();	// 获得光标处文字属性
    if (!StyleConstants.getForeground(attributeSet).equals(
        textColorCombo.getSelectedColor())) {
        textColorCombo.setSelectedColor(StyleConstants.getForeground(attributeSet));
    }
    boldCheckBox.setSelected(StyleConstants.isBold(attributeSet));
    italicCheckBox.setSelected(StyleConstants.isItalic(attributeSet));
    textSizeComboBox.setSelectedSize(StyleConstants.getFontSize(attributeSet));
}
```

#### 查找与替换

如图，当选中编辑菜单中的 `Find And Replace` 选项或按下 `Ctrl+R` 快捷键后，主界面 `BorderLayout` 中 `South` 部分将会称为一个查找与替换面板。

<img src=".\FindAndReplaceUI.png" alt="Find And Replace UI" style="zoom: 67%;" />

查找与替换功能的 `Find Next` 按钮可以在面板选中需要查找的下一个字符串，“查找下一个”功能是由第一次进行查找时，在文档中查找所有出现位置建立索引得到的。

```java
boolean isLocationsValid;
private ArrayList<ArrayList<Integer>> foundTextLocations;
int nowIndicateFoundTextArrayIndex, nowReplacedIncrement;

private synchronized void findInText(String pattern) {
    int startOffset = nowDocument.getDefaultRootElement().getStartOffset();
    int length = nowDocument.getDefaultRootElement().getEndOffset() - startOffset;
    String text = "";
    try {
        text = nowDocument.getText(startOffset, length);
    } catch (Exception e) {
        e.printStackTrace();
    }
    foundTextLocations = new ArrayList<ArrayList<Integer>>();
    nowIndicateFoundTextArrayIndex = -1;
    nowReplacedIncrement = 0;
    int indFound = 0;
    while ((indFound = text.indexOf(pattern, indFound)) != -1) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(indFound + startOffset);
        a.add(indFound + startOffset + pattern.length());
        foundTextLocations.add(a);
        indFound += pattern.length();
    }
    isLocationsValid = true;
}

private void showNextFoundText() {
    showFoundText();
    nowIndicateFoundTextArrayIndex += 1;
    if (nowIndicateFoundTextArrayIndex == foundTextLocations.size()) {
        nowIndicateFoundTextArrayIndex = 0;
    }
}

private void showFoundText() {
    if (nowIndicateFoundTextArrayIndex == -1) {
        nowIndicateFoundTextArrayIndex = 0;
    }
    ArrayList<Integer> found = foundTextLocations.get(nowIndicateFoundTextArrayIndex);
    textArea.requestFocusInWindow();
    textArea.select(found.get(0) + nowReplacedIncrement, found.get(1) + nowReplacedIncrement);
}

private boolean replaceFoundTextNext() {
    if (foundTextLocations.isEmpty()) {
        return false;
    }
    if (nowIndicateFoundTextArrayIndex == -1) {
        nowIndicateFoundTextArrayIndex = 1;
    }
    ArrayList<Integer> found = foundTextLocations.get(nowIndicateFoundTextArrayIndex == 0 ? foundTextLocations.size() - 1 : nowIndicateFoundTextArrayIndex - 1);
    textArea.requestFocusInWindow();
    textArea.select(found.get(0) + nowReplacedIncrement, found.get(1) + nowReplacedIncrement);
    String altText = findAndReplaceFrame.replaceField.getText();
    textArea.replaceSelection(altText);
    nowReplacedIncrement += altText.length() - findAndReplaceFrame.findField.getText().length();
    showNextFoundText();
    isLocationsValid = true;
    return true;
}
```

另外，在用户手动更改文档内容时（由 `DocumentListener` 监听），字段 `isLocationsValid` 将会被设置为 `false`，这时已经找到的索引将不再有效，“查找下一个功能将从头开始”。

替换功能因为更改了原串的长度，则查找得到的索引需要重新建立。***JaWa Editor*** 设置了一个追加偏移量避免重新执行耗时的查找操作。（具体见上述代码）

***JaWa Editor*** 也提供了单纯的查找功能，该功能与界面是“查找与替换”功能的子集，不再赘述。



### 保存功能与版本功能

#### JaWa 文档与版本功能



#### 本地保存功能



#### 远程保存功能





