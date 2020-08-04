
package com.sudarsono.user.util;

public interface BasePresenter {

    void onError(Throwable throwable);

    void subscribe();

    void unsubscribe();

}
