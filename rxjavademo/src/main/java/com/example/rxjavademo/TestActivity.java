package com.example.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjavademo.base.BaseActivity;
import com.example.rxjavademo.contract.LoginConstract;
import com.example.rxjavademo.model.LoginResponse;
import com.example.rxjavademo.presenter.LoginPresenter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends BaseActivity<LoginPresenter> implements LoginConstract.View {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // observe create
        test1();
        // dispose
        test2();
        // main thread & Consumer
        test3();
        // sub thread start Observer & main thread response
        test4();
        // switch many times thread
        test5();
        // map
        test6();
        // flatMap
        test7();
        // concatMap
        test8();
        // zip - 1 (same thread)
        test9_1();
        // zip - 2 (multi thread)
        //test9_2();

        // no backpressure (OOM：no zuo no die)
        // test10_1();

        // has limit 1）subscribeOn.filter
        test10_2();
        // has limit 2) .sample()
        test10_3();
        // has limit 3) slow down emit
        test10_4();

        // flowable
        test11_1();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void init() {
        mPresenter.doLogin("astaxie","11111");
    }


    @Override
    public void result(LoginResponse s) {
        Log.d(TAG, "test-login——" + s.toString());
    }


    private void test1() {
        //创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        };
        //建立连接
        observable.subscribe(observer);

        // 链式写法
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext("chained mode: " + 1);
                        emitter.onNext("chained mode: " + 2);
                        emitter.onNext("chained mode: " + 3);
                        emitter.onComplete();
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "subscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, "" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "complete");
                    }
                });
    }

    private void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "test2—— " + "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "test2—— " +"emit 2");
                emitter.onNext(2);
                Log.d(TAG, "test2—— " +"emit 3");
                emitter.onNext(3);
                Log.d(TAG, "test2—— " +"emit complete");
                emitter.onComplete();
                Log.d(TAG, "test2—— " +"emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
                i++;
                if (i == 2) {
                    Log.d(TAG, "test2—— " +"dispose");
                    mDisposable.dispose();
                    Log.d(TAG, "test2—— " +"isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });
    }

    private void test3() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "test3——Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "test3——emit 1");
                emitter.onNext("i'm test3——" + 1);
            }
        });

        Consumer<String> consumer = new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "test3—— onNext: " + s);
            }
        };

        observable.subscribe(consumer);
    }

    private void test4() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "test4——Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "test4——emit 1");
                emitter.onNext("I'm test4——" + 1);
            }
        });

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "test4——Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "test4——onNext accept: " + s);
            }
        };
        // on sub thread subscribe
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private void test5() {

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "test5——Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "test5——emit 1");
                emitter.onNext("I'm test5——" + 1);
            }
        });

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "test5——Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "test5——onNext accept: " + s);
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "test5——After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "test5——After observeOn(io), current thread is : " + Thread.currentThread().getName());
                    }
                })
                .subscribe(consumer);
    }

    private void test6() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "test6——This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "test6——accept: " + s);
            }
        });
    }

    private void test7() {
        Observable<String> query = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("test7——" + 1);
                emitter.onNext("test7——" + 2);
                emitter.onNext("test7——" + 3);
            }
        });
        query.flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                char[] chars = s.toCharArray();
                List<String> list = new ArrayList<String>();
                for(int i=0;i<chars.length;i++) {
                    list.add(s + "——" +chars[i] + "(" + i + ")");
                }
                return Observable.fromIterable(list).delay(100, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "test7—— accept: " + s);
            }
        });
    }

    private void test8() {
        Observable<String> query = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("test8——" + 1);
                emitter.onNext("test8——" + 2);
                emitter.onNext("test8——" + 3);
            }
        });
        query.concatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                char[] chars = s.toCharArray();
                List<String> list = new ArrayList<String>();
                for(int i=0;i<chars.length;i++) {
                    list.add(s + "——" +chars[i] + "(" + i + ")");
                }
                return Observable.fromIterable(list).delay(100, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "test8—— accept: " + s);
            }
        });
    }

    private void test9_1() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "test9_1——emit 1");
                emitter.onNext(1);
                Log.d(TAG, "test9_1——emit 2");
                emitter.onNext(2);
                Log.d(TAG, "test9_1——emit 3");
                emitter.onNext(3);
                Log.d(TAG, "test9_1——emit 4");
                emitter.onNext(4);
                Log.d(TAG, "test9_1——emit complete1");
                emitter.onComplete();
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "test9_1——emit A");
                emitter.onNext("test9_1——A");
                Log.d(TAG, "test9_1——emit B");
                emitter.onNext("test9_1——B");
                Log.d(TAG, "test9_1——emit C");
                emitter.onNext("test9_1——C");
                Log.d(TAG, "test9_1——emit complete2");
                emitter.onComplete();
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "test9_1——onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "test9_1——onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "test9_1——onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "test9_1——onComplete");
            }
        });


    }

    private void test9_2() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "test9_2——emit 1 @ " + Thread.currentThread().getName());
                emitter.onNext(1);
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit 2 @ " + Thread.currentThread().getName());
                emitter.onNext(2);
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit 3 @ " + Thread.currentThread().getName());
                emitter.onNext(3);
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit 4 @ " + Thread.currentThread().getName());
                emitter.onNext(4);
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit complete1");
                emitter.onComplete();
                //Thread.sleep(1000);
            }
        }).subscribeOn(Schedulers.io());    // 切换线程

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "test9_2——emit A @ " + Thread.currentThread().getName());
                emitter.onNext("test9_2——A");
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit B @ " + Thread.currentThread().getName());
                emitter.onNext("test9_2——B");
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit C @ " + Thread.currentThread().getName());
                emitter.onNext("test9_2——C");
                //Thread.sleep(1000);

                Log.d(TAG, "test9_2——emit complete2");
                emitter.onComplete();
                //Thread.sleep(1000);
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return "i'm observable1: " + integer + " >>> i'm observable1: " + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "test9_2——onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "test9_2——onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "test9_2——onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "test9_2——onComplete");
            }
        });

    }

    private void test10_1() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i<10000; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io());
        // 如果使用同1个线程，会等待上次完成依次执行，如果在不同线程，无背压下会OOM

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("i'm test10_1——A");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + " ," + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "test10_1——consumer accept: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.w(TAG, "test10_1——throw error: " + throwable);
            }
        });
    }

    private void test10_2() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; i<10000; i++) {
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 1000 == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "test10_2——consumer accept:" + integer);
                    }
                });

    }

    private void test10_3() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; i<10000; i++) {
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)            //sample取样，会丢失事件
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "test10_3——consumer accept:" + integer);
                    }
                });

    }

    private void test10_4() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; i<10000; i++) {
                            emitter.onNext(i);
                            Thread.sleep(2000);  //每次发送完事件延时2秒
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "test10_4——consumer accept: " + integer);
                    }
                });
    }


    private void test11_1() {
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "test11_1——emit 1");
                emitter.onNext(1);
                Log.d(TAG, "test11_1——emit 2");
                emitter.onNext(2);
                Log.d(TAG, "test11_1——emit 3");
                emitter.onNext(3);
                Log.d(TAG, "test11_1——emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); //增加了一个参数

        Subscriber<Integer> downstream = new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "test11_1——onSubscribe");
                s.request(Long.MAX_VALUE);  //注意这句代码
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "test11_1——onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "test11_1——onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "test11_1——onComplete");
            }
        };
        upstream.subscribe(downstream);
    }
}
