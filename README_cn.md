# Loading Progress Details

[English](README.md) | **中文**

一个面向 **Minecraft 1.21.11** 的 Fabric 客户端 Mod，在原版资源加载进度条
（Mojang 标志界面）下方显示详细的加载进度信息。

## 功能

资源重载期间（启动游戏或按 F3+T）会在进度条下方额外绘制两行信息：

- **第一行** —— 当前阶段（`正在准备资源` / `正在应用资源` / `重载完成`）、
  与进度条一致的百分比、已用时间。
- **第二行** —— 准备/应用的任务计数，以及尚未完成的加载器
  （如 `model_loader`、`sprite_uploader`），可以清楚看到卡在哪一步。

通过 Fabric API 注册的加载器会显示其 Fabric id，而不是混淆后的类名。

## 与 RRLS 的兼容性

本 Mod 专门设计为与
[RRLS (Remove Reloading Screen)](https://modrinth.com/mod/rrls) 共存：

- 它只在 `SplashOverlay#renderProgressBar` 方法尾部注入，不包裹、不重定向、
  不取消任何原版调用，因此与 RRLS 对同一方法内部的注入（仅用于给进度条
  换色/插值）互不冲突。
- 当 RRLS 配置为完全跳过加载界面时，它使用的假画布对全部绘制调用都是
  空实现，本 Mod 的文字会安静地消失，而不会崩溃。
- 当 RRLS 设置为 `PROGRESS` 模式时，详细信息同样会绘制在 RRLS 自己的
  进度条下方。

## 构建

```bash
./gradlew build
```

构建产物位于 `build/libs/`。

## 许可证

[MIT](LICENSE)
