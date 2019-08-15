package org.seckill.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * Created by zhuhp on 2017/2/16.
 */

public class CatInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(CatInterceptor.class);

    private ThreadLocal<Transaction> tranLocal = new ThreadLocal<Transaction>();
    private ThreadLocal<Transaction> pageLocal = new ThreadLocal<Transaction>();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {


        String uri = httpServletRequest.getRequestURI();
        Transaction transaction = Cat.newTransaction("URL", uri);

        Cat.logEvent("URL.Method", httpServletRequest.getMethod(), Message.SUCCESS, httpServletRequest.getRequestURL().toString());
        Cat.logEvent("URL.Host", httpServletRequest.getRemoteHost(), Message.SUCCESS, httpServletRequest.getRemoteHost());

        tranLocal.set(transaction);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String viewName = modelAndView != null ? modelAndView.getViewName() : "NONE";
        Transaction transaction = Cat.newTransaction("view", viewName);
        pageLocal.set(transaction);

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        Transaction pt = pageLocal.get();
        pt.setStatus(Transaction.SUCCESS);
        pt.complete();

        Transaction t = tranLocal.get();
        t.setStatus(Transaction.SUCCESS);
        t.complete();
    }
}
