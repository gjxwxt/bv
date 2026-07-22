# BV (Bug Video) 核心功能重构与可行性调研方案

## 1. 调研背景与对标分析（对标：云视听小电视 TV 客户端）

通过对比“云视听小电视”官方 TV 客户端与当前 BV 项目的差异，主要存在以下优化点：
1. **选集体验**：云视听小电视在播放画面右侧/底部弹出选集与合集列表，切换无缝不黑屏，且支持自动连播下一集/下一 P。
2. **分区探索**：云视听小电视将 UGC 分区（动画、游戏、科技、娱乐等）作为核心大类，支持二级分类筛选与热度/时间排序。
3. **遥控器焦点与播放控制**：遥控器方向键无盲区，播放器控制栏在 3 秒无操作后平滑淡出，且按键不会导致焦点丢弃。

---

## 2. 核心模块可行性调研与实现方案

### 模块 A：播放器选集/合集、画质降级与弹幕控制

#### A.1 多 P 选集与 PGC/UGC 合集连续播放
- **可行性分析**：100% 可实现。
- **数据源支持**：
  - UGC 多 P：`VideoDetail.pages` (包含 `cid`, `page`, `part`)。
  - PGC 番剧：`PgcSeason.episodes`。
  - 合集/列表：`UgcSeason.episodes`。
- **实现方案**：
  1. 在 `:app:shared` 或 `:app:tv` / `:app:mobile` 的 `VideoPlayerScreen` 中抽离统一的 `PlayerPlaylistDrawer` 组件。
  2. 选集面板触发时，不销毁播放器，仅更新播放器 `playUrl` 对应的新 `cid` 并自动跳转。
  3. 增加播放完毕监听 `Player.STATE_ENDED`，自动切换至下一 P (`currentIndex + 1`)。

#### A.2 解码器异常捕获与画质/解码自动降级
- **可行性分析**：100% 可实现。
- **实现方案**：
  1. 监听 `:player` 层的 `Player.Listener.onPlayerError`。
  2. 当发生硬解初始化失败 (`ERROR_CODE_DECODER_INIT_FAILED`) 或 Surface 异常时，记录当前时间戳与 `bvid`。
  3. 触发降级策略：
     - **第一阶**：软解/备用解码器兜底（如 Media3 失败切 VLC，或硬解切 FFmpeg 软解）。
     - **第二阶**：画质自动降级（如 4K 60fps 降级至 1080P60 -> 1080P），重新请求 `playUrl` 加载。

#### A.3 弹幕性能优化与控制
- **可行性分析**：100% 可实现。
- **实现方案**：
  1. 增加弹幕防挡字幕与区域屏蔽（屏蔽顶部 25% 或底部 25%）。
  2. 在 Android TV 端根据硬件性能对 `DanmakuView` 开启上限拦截（如屏显最多 30~50 条），防止爆显存丢帧。

---

### 模块 B：移动端与电视端“分区 (Zone)”真正落地

- **可行性分析**：100% 可实现。目前 `:bili-api` 已经准备好完整的 API 支持！
- **数据源现状**：
  - `:bili-api` 中的 `UgcRepository` 已经实现了 `getRegionFeedRcmd(ugcType: UgcTypeV2, page: UgcFeedPage)` 接口。
  - `UgcTypeV2` 枚举中已内置：`Anime` (动画), `Music` (音乐), `Dance` (舞蹈), `Gaming` (游戏), `Knowledge` (知识), `Tech` (科技), `Sports` (运动), `Car` (汽车), `Life` (生活), `Food` (美食), `Animal` (动物圈), `Kichiku` (鬼畜), `Fashion` (时尚), `Ent` (娱乐), `Cinephile` (影视)。
- **实现方案**：
  1. **移动端 (`MobileMainScreen.kt`)**：
     - 用 `ZoneScreen` 替换掉原有的 `DevelopingTipContent()` 占位。
     - 顶部放置 `ScrollableTabRow`（分类选项），内容区域为双列瀑布流 (`LazyVerticalStaggeredGrid`)。
  2. **电视端 (`UgcContent.kt`)**：
     - 结合遥控器焦点，左侧为分类列表，右侧为视频网格 (`LazyVerticalGrid`)，无缝响应 D-Pad 导航。

---

### 模块 C：电视端遥控器焦点 (Focus) 体验与稳定性优化

- **可行性分析**：100% 可实现。
- **实现方案**：
  1. **焦点流转逻辑**：
     - 使用 Compose for TV 的 `FocusRequester` 与 `onFocusChanged`，避免界面切换时“焦点失踪”。
     - 播放器控制栏在按下方向键或按键时弹出，并启动 `3 秒` 自动隐藏计时器。
  2. **Wbi 鉴权防失效**：
     - 维护 `bili-api` 里的 `WbiUtil`，确保实时解析 `img_key` 和 `sub_key` 并计算 `w_rid`，保障高画质接口鉴权成功率。

---

## 3. 推荐的开发优先级与路线图

| 阶段 | 核心任务 | 预估复杂度 |
| :--- | :--- | :--- |
| **Phase 1** | **分区 (Zone) 模块全面落地** (利用已现有的 `UgcRepository` 快速替换占位组件) | ★★☆☆☆ (低风险高见效) |
| **Phase 2** | **播放器多 P 选集/合集连续播放与选集面板** | ★★★☆☆ (中) |
| **Phase 3** | **播放器软硬解/画质自动降级与弹幕控制优化** | ★★★☆☆ (中) |
| **Phase 4** | **TV 端遥控器焦点流畅度与 Wbi 鉴权稳定性增强** | ★★☆☆☆ (中) |
