https://github.com/dromara/dante-cloud/blob/master/platform/dante-cloud-gateway/src/main/java/cn/herodotus/dante/gateway/utils/WebFluxUtils.java
## basic structure

```
root
    core
        core-common
        core-dependencies
        core-starter
            core-starter-web
    service-biz
    service-support
```


## feature

- 统一的依赖管理, 配置管理
- 可插拔式组件
- 全局异常处理
- 全局日期格式自动替换
- 响应数据统一封装
- 组件优化封装: redis, Mybatis, rocketmq

## env requirement

```shell



```

## trade engine

```
API模块（Trading API），交易员下单、撤单的API入口；
定序模块（Sequencer），用于对所有收到的订单进行定序；
交易引擎（Trading Engine），对定序后的订单进行撮合、清算；
    资产模块：管理用户的资产；
    订单模块：管理用户的活动订单（即尚未完全成交且未取消的订单）；
    撮合引擎：处理买卖订单，生成成交信息；
    清算模块：对撮合引擎输出的成交信息进行清算，使买卖双方的资产进行交换。
行情模块（Quotation），将撮合输出的成交信息汇总，形成K线图；
推送模块（Push），将市场行情、交易结果、资产变化等信息以WebSocket等途径推送给用户；
UI模块（UI），给交易员提供一个Web操作界面，并把交易员的操作转发给后端API。

交易引擎是一个以事件驱动为核心的系统，它的输入是定序后的一个个事件，输出则是撮合结果、市场行情等数据。交易引擎内部各模块关系如下：

  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
     ┌─────────┐    ┌─────────┐
──┼─▶│  Order  │───▶│  Match  │ │
     └─────────┘    └─────────┘
  │       │              │      │
          │              │
  │       ▼              ▼      │
     ┌─────────┐    ┌─────────┐
  │  │  Asset  │◀───│Clearing │ │
     └─────────┘    └─────────┘
  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘

```
