##app项目中列表页面的封装实现

> 客户端项目常常遇到很多列表页面，而这些列表页面都很相似，无论逻辑还是ui，所以封装一个公用的列表页面可以极大地减轻开发负担，提高程序的可维护性。

#### 列表页面的需求整理

- 列表展示(必须支持)
- 空页面展示（非必须）
- 下拉刷新（非必须）
- 上拉加载更多（非必须）
- header展示（非必须）
- 分割线（非必须）

上面就是一般情况下列表页面的核心需求，根据需求整理其公共逻辑和差异逻辑。

| 需求         | 公共逻辑                                                     | 差异逻辑                                                     |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 列表展示     | 把列表每一个条目抽象成ItemView接口，把要展示的数据抽象成ViewBean接口 | 1.ui不同。2.点击事件处理不同                                 |
| 空白页面展示 | 空页面是一个view                                             | 1.就wdg来说，空页面有俩个元素—icon，和提示文字。其中只有提示文字有变化。 |
| 下拉刷新     | 下拉刷新页面（重新请求网络接口）                             |                                                              |
| 上拉加载更多 | 上拉加载更多 （请求分页数据）                                |                                                              |
| header展示   | header是一个view                                             | 1.ui不同。 2.点击事件不同。3.其它复杂交互                    |
| decoration   | 分割线展示                                                   | 1.左右间距。2.颜色。3.粗细。4.挨着header的分割线和底部分割线的绘制控制 |

### 实现方式

- 所有特性可配置

> 上面分析了列表页需要支持的特性，但是实际需求可能是某几个特性的组合，或者全部支持。这样把所有特性都做成可配置的，那么就比较灵活。

所以定义了一个`ListFragmentConfig`类，其成员变量为支持的特性和其它变量（列表元素的布局，header的布局等等）

- 上下拉的支持

> 下拉刷新和上拉支持更多用开源库RefreshLayout支持。在view层上下拉的操作是一样的，变化的是数据接口以及数据不同，这里委托给Presenter层来处理。

- 空页面和header的支持

> header的支持—— 使用WrapperedAdapter实现，本质上还是RecyclerView.Adapter,每一个header都是一种类型的ItemView。header除了展示ui，还需要对数据进行一些操作，这里需要对Presenter进行操作。

- 列表展示

> 列表展示最主要的变化是数据不同每一项展示的ui不同，另外点击事件的处理不同，或许还有更复杂的逻辑处理。对于这些变化我们不在公共列表页处理，委托给其它view层元素（headerview，parentFragment，Activity等等）

根据上述分析，抽象出几个接口

1. `ItemView`—— 表示列表中的一个ui元素
2. `ViewBean`—— 表示列表元素要渲染到界面的数据
3. `UsePresenter`—— 表示要使用Presenter
4. 定义一个公共适配器把`ItemView`和`ViewBean`联系起来

```java
public class CommonRecyclerViewAdapter<VB extends ViewBean> extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder<? extends ItemView>>
```



几个接口的具体代码如下

```java
public interface ItemView<T extends ViewBean> {
    void bindData(T bean);
}

public interface ViewBean extends Serializable{
}

public interface UsePresenter<T extends BasePresenter> {
    default void attachPresenter(T presenter) {}
}
```

- MVP架构

定义公共列表的Presenter层BaseListPresenter，抽象出其公共逻辑—— 设置请求参数，刷新，加载更多 ，消息总线订阅和解订阅。

```java
public interface BaseListPresenter extends BasePresenter {

	/**
     * 刷新数据
     */
    default void refreshRequest(){};

	/**
     * 加载更多数据
     */
    default void loadMore(){};

    /**
     * 设置额外的参数
     */
    default void setExtraParams(Map extraParams){}

    default void registBus(){
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    default void unRegisterBus(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
```

定义公共列表的View层BaseListView,抽象出其公共逻辑—— 展示数据列表到ui上，根据数据渲染header，展示空页面，清空列表。

```java
public interface BaseListView extends BaseView {
    void renderListData(List<? extends ViewBean> viewBeans, boolean isRefresh, boolean hasMore);

    default void renderHeaderView(ViewBean viewBean){}

    default void showEmptyView(){};

    default void clearList(){};
}
```

上述就是实现层面的一些抽象设计。具体代码和其它细节处理可查看wdg项目相关源码。



### 使用例子

以wdg项目交易页下部分的几个tab为例

```java
		//持仓
        ListFragmentConfig config = ListFragmentConfig.newBuilder()
            .orientation(OrientationHelper.VERTICAL)
            .viewBeanRes(R.layout.wdg_itemview_trade_hold)
            .listEmptyRes(R.layout.wdg_trade_empty_no_record)
            .isSupportRefresh(false)
            .isSupportLoadMore(false)
            .isAutoSubsribe(false)
            .isSupportEventBus(true)
            .listHeaderRes(R.layout.wdgapp_header_trade_hold)
            .isSupportEmpty(true)
            .aClass(WdgTradeHoldPresenter.class)
            .extraParams(extraParams)
            .emptyMsg(getString(R.string.wdgapp_no_holds))
            .build();
        WdgListFragment listFragment = WdgListFragment.newInstance(config);

        //挂单
        config = ListFragmentConfig.newBuilder()
            .orientation(OrientationHelper.VERTICAL)
            .viewBeanRes(R.layout.wdg_itemview_trade_order)
            .listEmptyRes(R.layout.wdg_trade_empty_no_record)
            .isSupportEmpty(true)
            .isSupportRefresh(false)
            .isSupportEventBus(true)
            .isAutoSubsribe(false)
            .isSupportLoadMore(false)
            .extraParams(extraParams)
            .aClass(WdgTradePendingOrderPresenter.class)
            .build();
        WdgListFragment pendingOrderFragment = WdgListFragment.newInstance(config);

        //委托
        config = ListFragmentConfig.newBuilder().orientation(OrientationHelper.VERTICAL)
            .viewBeanRes(R.layout.wdg_itemview_trade_order)
            .listEmptyRes(R.layout.wdg_trade_empty_no_record)
            .isSupportEmpty(true)
            .isSupportRefresh(false)
            .isSupportLoadMore(true)
            .extraParams(extraParams)
            .aClass(WdgTradeEntrustOrderPresenter.class)
            .build();
        WdgListFragment entrustFragment = WdgListFragment.newInstance(config);

        //成交
        config = ListFragmentConfig.newBuilder().orientation(OrientationHelper.VERTICAL)
            .viewBeanRes(R.layout.wdg_itemview_trade_deal)
            .listEmptyRes(R.layout.wdg_trade_empty_no_record)
            .isSupportEmpty(true)
            .isSupportRefresh(false)
            .isSupportLoadMore(false)
            .extraParams(extraParams)
            .aClass(WdgTradeDealPresenter.class)
            .build();
        WdgListFragment dealFragment = WdgListFragment.newInstance(config);
```

我们只需要根据需求配置即可，不关心列表页的具体实现。至于其它的变化我们可按如下一套规范来开发,以交易页持仓列表为例

- 数据层的实现—— 根据接口新建`ViewBean`的实现类（列表元素要展示的数据bean）。

  ```java
  public static class Hold implements ViewBean {
          @SerializedName("currency_id")
          public String currencyId;

          @SerializedName("currency_mark")
          public String currencyMark;

          public String num;

          public String forzennum;

          @SerializedName("currency_all_money")
          public String currencyAllMoney;

          public String price;
  }
  ```


- 数据请求的实现—— 定义一个BaseListPresenter的实现类，实现其“刷新”/“加载更多”逻辑，根据需求实现其它逻辑。

  ```java
  public class WdgTradeHoldPresenter implements BaseListPresenter {

      private BaseListView mView;
      private String mCurrencyId;
      private CompositeDisposable mComposite;

      public WdgTradeHoldPresenter(BaseListView mView) {
          this.mView = mView;
      }

      @Override
      public void setExtraParams(Map extraParams) {
          if (extraParams == null) {
              return;
          }
          mCurrencyId = (String) extraParams.get("currencyId");
      }

      @Override
      public void subscribe() {
          registBus();
          visible();
      }

      private void visible() {
          if (mComposite == null || mComposite.isDisposed()) {
              mComposite = new CompositeDisposable();
          }
          Disposable timerDispose = Flowable.interval(0, 3, TimeUnit.SECONDS)
                  .onBackpressureDrop()
                  .observeOn(SchedulerProvider.getInstance().ui())
                  .subscribe(aLong -> {
                      //请求持仓数据
                       Disposable dispose = NetworkManager.getInstance().getApiInterface()
                              .getTradeHold("")
                              .compose(SchedulerProvider.getInstance().applySchedulers())
                              .subscribe(response -> {
                                  mView.renderListData(response.getChichang(), true, false);
                              }, throwable -> {});
                      mComposite.add(dispose);
                  }, throwable -> {});
          mComposite.add(timerDispose);
      }

      @Override
      public void unsubscribe() {
          unRegisterBus();
          invisible();
      }

      private void invisible() {
          disPose(mComposite);
      }

      @Subscribe(threadMode = ThreadMode.MAIN)
      public void onEventHoldResponse(WdgTradeHoldResponse response) {
          mView.renderListData(response.getChichang(), true, false);
      }

      @Subscribe(threadMode = ThreadMode.MAIN)
      public void onEventSubscribe(WdgSubscribeEvent event) {
          if (event.isSubscribe()) {
              this.visible();
          } else {
              this.invisible();
          }
      }

      @Subscribe(threadMode = ThreadMode.MAIN)
      public void onEventCancelOrder(WdgCancelTradeEvent event) {
          refreshRequest();
      }

      @Subscribe(threadMode = ThreadMode.MAIN)
      public void onEventOrderSuccess(WdgOrderSuccessEvent event) {
          refreshRequest();
      }

      @Subscribe(threadMode = ThreadMode.MAIN)
      public void onEventTabChange(WdgTradeTabChangeEvent event) {
          if (event.pageIndex == 0) {
              this.visible();
          }
      }
  }
  ```

- 根据设计图实现xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <com.wdg.tradecenter.pages.trade.widgets.WdgTradeHoldRvItemView
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="55dp"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools">

      <android.support.constraint.Guideline
          android:id="@+id/guideline"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:orientation="vertical"
          app:layout_constraintGuide_percent="0.5466" />

      <TextView
          android:id="@+id/tv_ico_name"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          tools:text="WDG"
          android:textColor="@color/wdgapp_color_333333"
          android:textSize="15sp"
          app:layout_constraintLeft_toLeftOf="parent"
          app:layout_constraintTop_toTopOf="parent"
          app:layout_constraintBottom_toBottomOf="parent"
          android:layout_marginLeft="15dp"
          />

      <TextView
          android:id="@+id/tv_quantity"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          tools:text="10000"
          android:textColor="@color/wdgapp_color_333333"
          android:textSize="14sp"
          app:layout_constraintRight_toLeftOf="@+id/guideline"
          app:layout_constraintTop_toTopOf="parent"
          app:layout_constraintBottom_toTopOf="@+id/tv_frozen"
          app:layout_constraintVertical_chainStyle="packed"
          />

      <TextView
          android:id="@+id/tv_frozen"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          tools:text="10"
          android:textColor="@color/wdgapp_color_999999"
          android:textSize="10sp"
          app:layout_constraintRight_toLeftOf="@+id/guideline"
          app:layout_constraintTop_toBottomOf="@+id/tv_quantity"
          android:layout_marginTop="1dp"
          app:layout_constraintBottom_toBottomOf="parent"
          />

      <TextView
          android:id="@+id/tv_value"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          tools:text="10000"
          android:textColor="@color/wdgapp_color_333333"
          android:textSize="14sp"
          app:layout_constraintRight_toRightOf="parent"
          app:layout_constraintTop_toTopOf="parent"
          app:layout_constraintBottom_toTopOf="@+id/tv_price"
          app:layout_constraintVertical_chainStyle="packed"
          android:layout_marginRight="15dp"
          />

      <TextView
          android:id="@+id/tv_price"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          tools:text="10"
          android:textColor="@color/wdgapp_color_999999"
          android:textSize="10sp"
          app:layout_constraintRight_toRightOf="parent"
          app:layout_constraintTop_toBottomOf="@+id/tv_value"
          android:layout_marginTop="1dp"
          app:layout_constraintBottom_toBottomOf="parent"
          android:layout_marginRight="15dp"
          />

      <View
          android:layout_width="match_parent"
          android:layout_height="1px"
          android:background="@color/wdgapp_color_eeeeee"
          android:layout_marginLeft="15dp"
          app:layout_constraintBottom_toBottomOf="parent"
          app:layout_constraintLeft_toLeftOf="parent"
          app:layout_constraintRight_toRightOf="parent"
          />

  </com.wdg.tradecenter.pages.trade.widgets.WdgTradeHoldRvItemView>
  ```

- 根据xml新建一个`ItemView`的实现类，实现其"绑定数据"逻辑。

  ```java
  public class WdgTradeHoldRvItemView extends ConstraintLayout implements ItemView<WdgTradeHoldResponse.Hold> {
      @BindView(R.id.tv_ico_name)
      TextView tvIcoName;
      @BindView(R.id.tv_quantity)
      TextView tvQuantity;
      @BindView(R.id.tv_frozen)
      TextView tvFrozen;
      @BindView(R.id.tv_value)
      TextView tvValue;
      @BindView(R.id.tv_price)
      TextView tvPrice;

      public WdgTradeHoldRvItemView(Context context) {
          super(context);
      }

      public WdgTradeHoldRvItemView(Context context, AttributeSet attrs) {
          super(context, attrs);
      }

      public WdgTradeHoldRvItemView(Context context, AttributeSet attrs, int defStyleAttr) {
          super(context, attrs, defStyleAttr);
      }

      @Override
      protected void onFinishInflate() {
          super.onFinishInflate();
          ButterKnife.bind(this);
      }

      @Override
      public void bindData(WdgTradeHoldResponse.Hold bean) {
          if (bean == null) {
              return;
          }
          tvIcoName.setText(bean.getCurrencyMark());
          tvQuantity.setText(FormatUtil.formatWdgQuantity(bean.getNum()));
          tvFrozen.setText(FormatUtil.formatWdgQuantity(bean.getForzennum()));
          tvValue.setText(bean.getCurrencyAllMoney());
          tvPrice.setText(bean.getPrice());
      }
  }
  ```

- 最后把这些变化和需求所要支持的特性配置，然后`WdgListFragment`根据这些配置构建列表页

  ```java
  //持仓
          ListFragmentConfig config = ListFragmentConfig.newBuilder()
              .orientation(OrientationHelper.VERTICAL)
              .viewBeanRes(R.layout.wdg_itemview_trade_hold)
              .listEmptyRes(R.layout.wdg_trade_empty_no_record)
              .isSupportRefresh(false)
              .isSupportLoadMore(false)
              .isAutoSubsribe(false)
              .isSupportEventBus(true)
              .listHeaderRes(R.layout.wdgapp_header_trade_hold)
              .isSupportEmpty(true)
              .aClass(WdgTradeHoldPresenter.class)
              .extraParams(extraParams)
              .emptyMsg(getString(R.string.wdgapp_no_holds))
              .build();
          WdgListFragment listFragment = WdgListFragment.newInstance(config);
  ```



### 更多使用姿势

- 点击事件的处理,实现`CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner<WdgMarketListResponse.Currency> `接口

  ```java
  @Override
      public void onItemClick(View view, RecyclerView rv, int position, WdgMarketListResponse.Currency data) {
          WdgCoinDetailActivity.start(getContext(), data.currency_id);
      }
  ```

- ItemView中的多个控件的点击事件处理—— 重写点击事件，并把点击监听派发给需要处理的控件

  ```java
  @Override
      public void setOnClickListener(@Nullable OnClickListener l) {
          tvCancel.setOnClickListener(l);
      }
  ```

- header实现

  ```java
  public class WdgIcoPropertyHeaderView extends ConstraintLayout implements ItemView<WdgPropertyResponse> {

      private TextView tvAll;
      private ImageView ivHide;

      private FrameLayout flDeposit;
      private FrameLayout flWithDraw;

      private WdgPropertyResponse mResponse;

      public WdgIcoPropertyHeaderView(Context context) {
          super(context);
      }

      public WdgIcoPropertyHeaderView(Context context, AttributeSet attrs) {
          super(context, attrs);
      }

      public WdgIcoPropertyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
          super(context, attrs, defStyleAttr);
      }

      @Override
      protected void onFinishInflate() {
          super.onFinishInflate();
          tvAll = findViewById(R.id.tv_property_content);
          ivHide = findViewById(R.id.iv_crypt_state);
          flDeposit = findViewById(R.id.fl_deposit);
          flWithDraw = findViewById(R.id.fl_withdraw);

          flDeposit.setOnClickListener(v -> {
              WdgChargeCoinActivity.start(getContext());
          });

          flWithDraw.setOnClickListener(v -> {
              Context context = getContext();
              context.startActivity(new Intent(context, WdgWithdrawCoinActivity.class));
          });

          ivHide.setOnClickListener(v -> {
              boolean showMoney = SPUtils.getInstance().getBoolean(WdgCons.SP_KEY_SHOW_MONEY, true);
              showMoney = !showMoney;
              SPUtils.getInstance().put(WdgCons.SP_KEY_SHOW_MONEY, showMoney);
              updateMoneyAboutView(showMoney);
              RecyclerView rv = getParentRecyclerView(this);
              if (rv != null && rv.getAdapter() != null) {
                  rv.getAdapter().notifyDataSetChanged();
              }
          });
      }

      @Override
      public void bindData(WdgPropertyResponse bean) {
          if (bean == null || !bean.isSucceed()) {
              return;
          }
          mResponse = bean;
          boolean showMoney = SPUtils.getInstance().getBoolean(WdgCons.SP_KEY_SHOW_MONEY, true);
          updateMoneyAboutView(showMoney);
      }

      private void updateMoneyAboutView(boolean showMoney) {
          if (mResponse == null) {
              return;
          }
          if (showMoney) {
              tvAll.setText(mResponse.getTotalMoneyRaw());
              ivHide.setImageResource(R.drawable.wdgapp_icon_property_state_show);
          } else {
              ivHide.setImageResource(R.drawable.wdgapp_icon_property_state_hide);
              tvAll.setText(WdgPropertyFragment.CRYPT_SYMBOLS);
          }
      }

      private static RecyclerView getParentRecyclerView(@Nullable View view) {
          if (view == null) {
              return null;
          }
          ViewParent parent = view.getParent();
          if (parent instanceof RecyclerView) {
              return (RecyclerView) parent;
          } else if (parent instanceof View) {
              return getParentRecyclerView((View) parent);
          } else {
              return null;
          }
      }
  }
  ```


### 总结

本文整理了公共列表页的需求，设计和实现过程，使用的例子，以及使用规范。当然在实现细节和需求满足上还有很多瑕疵，希望大家共同完善，在保留接口不变的情况下提出更好的代码实现。尽管这个小框架不太完善，但优点也是显而易见的：

- 把类似需求的变化项分解成清晰的互不相干的粒度进行处理，把公共的逻辑统一处理，提供更清晰的代码实现思维，减少出错的机率
- 更好的维护性—— 假如需要修改空页面的样式，我们可以统一处理，而不必修改多处代码。对于特殊的空页面，框架也支持自定义布局设置进去/或者自己控制header进行show和hide的操作。

### 后续支持的特性

- 灵活的分割线支持
列表的参数类是ListFragmentConfig,分割线的参数类是DecorationConfig.如下所示
```
 DecorationConfig implements Serializable {
     int dividerHeight;          //分割线的高度
    float leftOffset;              //分割线离左边框的距离
    float rightOffset;           //分割线离右边框的距离
    int dividerColorRes;    //分割线的颜色
    int style;                     //分割线的风格,比如头部不含分割线,从第二个view开始绘制分割线等
	...
}

 // 分割线的使用
  DecorationConfig decorationConfig=new DecorationConfig.Builder()
                .dividerHeight(1)
                .leftOffset(0)
                .rightOffset(0)
                .dividerColorRes(R.color.wdgapp_color_eeeeee)
                .style(WdgRVItemSplitDecoration.STYLE_NO_FIRST_DIVIDER)
                .build();

        config = ListFragmentConfig.newBuilder().orientation(OrientationHelper.VERTICAL)
            .viewBeanRes(R.layout.wdg_itemview_trade_order)
            .listEmptyRes(R.layout.wdg_trade_empty_no_record)
            .isSupportEmpty(true)
            .isSupportRefresh(false)
            .isSupportLoadMore(true)
            .decorationConfig(decorationConfig)     //分割线设置
            .extraParams(extraParams)
            .aClass(WdgTradeEntrustOrderPresenter.class)
            .build();
        WdgListFragment entrustFragment = WdgListFragment.newInstance(config);
```

