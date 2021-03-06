package com.example.xh.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xh.R;
import com.example.xh.rxjava.NormalRxActivity;
import com.example.xh.rxjava.RxConnetActivity;
import com.example.xh.rxjava.RxFilterActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

/**
 * Created by xiehui on 2016/10/31.
 */
public class TestRxJavaFragment extends Fragment implements View.OnClickListener {

    private String TAG = "RXJAVA";
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12;
    private LinearLayout layout;
    private TextView tv;
    private StringBuffer stringBuffer;
    private Subscription subscription;

    //a library for composing asynchronous and event-based programs using observable sequences for the Java VM"
    // （一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库）
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rxjavafragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn3 = (Button) view.findViewById(R.id.btn3);
        btn4 = (Button) view.findViewById(R.id.btn4);
        btn5 = (Button) view.findViewById(R.id.btn5);
        btn6 = (Button) view.findViewById(R.id.btn6);
        btn7 = (Button) view.findViewById(R.id.btn7);
        btn8 = (Button) view.findViewById(R.id.btn8);
        btn9 = (Button) view.findViewById(R.id.btn9);
        btn10 = (Button) view.findViewById(R.id.btn10);
        btn11 = (Button) view.findViewById(R.id.btn11);
        btn12 = (Button) view.findViewById(R.id.btn12);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        tv = (TextView) view.findViewById(R.id.tv);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {


                subscriber.onNext("hi RxJAVA");
                subscriber.onCompleted();

            }
        });

        //subscriber观察者,去处理observable发射过来的数据
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };

        observable.subscribe(subscriber);
        Observable.just("hi RXJAVA2").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        });
        String[] strs = {"1", "2", "3", "4"};
        Observable.from(Arrays.asList(strs))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, s);
                    }
                });

        Observable.just("hi Rxjava3")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                Log.e(TAG, s + "");
            }
        });

        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                String[] strs = {"1", "2", "3", "4", "5", "6", "7"};
                subscriber.onNext(Arrays.asList(strs));
            }
        }).flatMap(new Func1<List<String>, Observable<?>>() {
            @Override
            public Observable<?> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).filter(new Func1<Object, Boolean>() {//filter 操作符，去掉“1”;
            @Override
            public Boolean call(Object o) {
                if (o.toString().equals("1")) return false;
                return true;
            }
        })
                .take(2)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Log.e(TAG, o.toString());
                    }
                });

        //灯和开关的例子，灯观察者，开关被观察者
        //创建被观察者
        Observable switcher = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("On");
                subscriber.onNext("Off");
                subscriber.onNext("On");
                subscriber.onNext("On");
                subscriber.onCompleted();
            }
        });
        //上面创建也可写为
        Observable switcher1 = Observable.just("On", "Off", "On", "On");
        //或者
        String[] strs1 = {"On", "Off", "On", "On"};
        Observable switcher2 = Observable.from(strs1);
        //创建观察者，灯
        Subscriber light = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "灯结束观察");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "出现错误");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "传递过来的信息" + s);
            }
        };

        switcher.subscribe(light);


        Observable.just("ON1", "OFF1", "ON1", null)
                //指定了被观察者执行的线程环境
                .subscribeOn(Schedulers.newThread())
                //过滤空值
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null;
                    }
                })
                //实现订阅
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, s + "");
                    }
                });
        //subscribeOn（）它指示Observable在一个指定的调度器上创建（只作用于被观察者创建阶段）。只能指定一次，如果指定多次则以第一次为准
        //observeOn（）指定在事件传递（加工变换）和最终被处理（观察者）的发生在哪一个调度器。可指定多次，每次指定完都在下一步生效。
        //create     RxJava 最基本的创造事件序列的方法
        //在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；在哪个线程生产事件，
        // 就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler调度器
        //Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler
        //Schedulers.newThread(): 总是启用新线程，并在新线程执行操作
        //Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于
        // io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io()
        // 中，可以避免创建不必要的线程

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            // FuncX 包装的是有返回值的方法,ActionX 无
            //Action0中call无参数，Action1有参数
            //变换实质针对事件序列的处理和再发送
            //lift() 是针对事件项和事件序列的，而 compose() 是针对 Observable 自身进行变换
            case R.id.btn1:
                intent.setClass(getContext(), NormalRxActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                intent.setClass(getContext(), RxConnetActivity.class);
                startActivity(intent);
                break;
            case R.id.btn3:
                intent.setClass(getContext(), RxFilterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn4:
                //所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列
                //map() 是一对一的转化
                executeMap();
                break;
            case R.id.btn5:
                //可以一对多
                // flatMap() 中返回的是个 Observable 对象，并且这个 Observable 对象并不是被直接发送到了 Subscriber 的回调方法中
                //

                executeFlatMap();
                break;
            case R.id.btn6:
                executeSort();
                break;
            case R.id.btn7:
                executeTake();
                break;
            case R.id.btn8:
                executeMerge();
                break;
            case R.id.btn9:
                executeSchedulers();
                break;
            case R.id.btn10:
                executeInterval();
                break;
            case R.id.btn11:
                executeUnsubscribe();
                break;
            case R.id.btn12:
                executeTimestamp();
                break;
        }
    }

    private void executeTimestamp() {
        Integer[] number = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        tv.setText("输入数据：1, 2, 3, 4, 5, 6, 7, 8, 9, 10\n");
        Observable.from(number).timestamp().subscribe(new Action1<Timestamped<Integer>>() {
            @Override
            public void call(Timestamped<Integer> integerTimestamped) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                tv.append("value: " + integerTimestamped.getValue() + "       time:   ");
                tv.append(sdf.format(new Date(integerTimestamped.getTimestampMillis())) + "\n");
            }
        });
    }

    private void executeUnsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void executeInterval() {
        tv.setText("定时器，每一秒发送打印一个数字\ninterval(1, TimeUnit.SECONDS)\n");
        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tv.append(" " + aLong + "   ");
                    }
                });


    }

    private void executeSchedulers() {
        //subsribeOn( )可以改变Observable应该在哪个调度器上执行任务。
        //subscribeOn()主要改变的是订阅的线程，即call()执行的线程;observeOn()主要改变的是发送的线程，即onNext()执行的线程。
        //subscribeOn则是一次性的，无论在什么地方调用，总是从改变最原始的observable开始影响整个observable的处理
        //observeOn( )操作符可以改变Observable将事件发送到得线程，也就是Subscriber执行的线程,可多次执行
        //默认情况下，链上的操作符将会在调用.subsribeOn( )的那个线程上执行任务
        stringBuffer = new StringBuffer();
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                //不能执行耗时操作，及更新ui
                stringBuffer.append("\n" + "开始发送事件" + Thread.currentThread().getName() + "\n");
                Drawable drawable = getResources().getDrawable(R.mipmap.dir);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Drawable, ImageView>() {
                    @Override
                    public ImageView call(Drawable drawable) {
                        ImageView imageView = new ImageView(getActivity());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageView.setLayoutParams(params);
                        imageView.setImageDrawable(drawable);

                        return imageView;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ImageView>() {
                    @Override
                    public void call(ImageView imageView) {
                        tv.append(stringBuffer.toString() + "接收信息事件" + Thread.currentThread().getName());
                        layout.addView(imageView);
                    }
                })
        ;


    }

    private void executeMerge() {
        tv.setText("并发执行任务开始\n");
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(500);
                    subscriber.onNext("aaaaaaaaaa");
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.newThread());
        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(500);
                    subscriber.onNext("bbbbbbbbbb");
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.newThread());
        Observable.merge(observable, observable1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv.append("两个任务都处理完毕！！\n");
                        tv.append("更新数据：\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv.append("得到一个数据：" + s + "\n");
                    }
                });
    }

    private void executeTake() {
        tv.setText("输出[1,2,3,4,5,6,7,8,9,10]中第三个和第四个奇数，\ntake(i) 取前i个事件 \ntakeLast(i) 取后i个事件 \ndoOnNext(Action1) 每次观察者中的onNext调用之前调用\n");
        Integer[] number = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Observable.from(number).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 != 0;//过滤掉偶数
            }
        })
                //取前四个值
                .take(4)
                ////取前四个中的后两个,每次调用是作用在前面的基础上的数据
                .takeLast(2)
                //doOnNext作用是啥?允许我们在每次输出一个元素之前做一些额外的事情
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tv.append("before onNext（）" + integer + "\n");
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                tv.append("onNext()--->" + integer + "\n");
            }
        });
    }

    private void executeSort() {
        tv.setText("输入参数： 2,3,6,4,2,8,2,1,9");
        Integer[] integers = {2, 3, 6, 4, 2, 8, 2, 1, 9};
        Observable.from(integers)
                .toSortedList()
                .flatMap(new Func1<List<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(List<Integer> integer) {
                        return Observable.from(integer);
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                tv.append("\n" + integer);
            }
        });
    }

    private void executeFlatMap() {
        tv.setText("输入参数： 2,3,6,4,2,8,2,1,9");
        Integer[] integers = {2, 3, 6, 4, 2, 8, 2, 1, 9};
        Observable.from(integers).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just((integer + 100) + "FlatMap");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                tv.append("\n 转换后的内容：" + s + "\n");
            }
        });
    }

    private void executeMap() {

        tv.setText("输入参数： 0,0,6,4,2,8,2,1,9,0,23");
        Integer[] integers = {0, 0, 6, 4, 2, 8, 2, 1, 9, 0, 23};
        Observable.from(integers).map(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return (integer > 5);
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                tv.append("\n观察到输出结果：");
                tv.append(aBoolean.toString() + "\n");
            }
        });
    }

}
