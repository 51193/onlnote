## 目录结构

```
.
├── user-name
│   ├── folder-name
│   │   ├── blog-name.dark.html
│   │   ├── blog-name.light.html
│   │   ├── blog-name.md
│   │   └── *
│   └── *
└── *
```
支持目录嵌套。

隐藏目录将被无视。

## 文件名结构

对于统一个博文，要求统一名称`[blog-name]`，采用以下规则区分样式类型`[theme-type]`和文件类型`[file-type]`。

`[blog-name].[theme-type].[file-type]`。其中`[theme-type]`为可选项。

* `[theme-type] -> light | dark`
* `[file-type] -> md | html`

例如：博文名为`这是一个测试文章`，源文件格式为`md`，导出了亮暗两个主题的`html`文件。则将这三个文件同时存放在相同目录下，并分别命名为`这是一个测试文章.md`、`这是一个测试文章.light.html`、`这是一个测试文章.dark.html`。