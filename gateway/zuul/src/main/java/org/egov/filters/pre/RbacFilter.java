package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.contract.Action;
import org.egov.contract.User;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.egov.constants.RequestContextConstants.ERROR_CODE_KEY;
import static org.egov.constants.RequestContextConstants.ERROR_MESSAGE_KEY;
import static org.egov.constants.RequestContextConstants.USER_INFO_KEY;

public class RbacFilter extends ZuulFilter{

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private List<String> rbacWhitelist;
    public RbacFilter(List<String> rbacWhitelist){
        this.rbacWhitelist = rbacWhitelist;
    }

    @Override
    public String filterType() {return "pre";}

    @Override
    public int filterOrder() {return 3;}

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        boolean shoudDoAuth = ctx.getBoolean("shouldDoAuth");
        return shoudDoAuth && rbacWhitelist.stream().anyMatch(url -> ctx.getRequest().getRequestURI().contains(url));
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        User user = (User) ctx.get(USER_INFO_KEY);
        if(user.getActions().stream().anyMatch(action -> requestUri.equals(action.getUrl())))
            return null;

        abortWithStatus(ctx,HttpStatus.NOT_FOUND,"The resource you are trying to find is not available");
        return null;
    }


    private void abortWithStatus(RequestContext ctx, HttpStatus status, String message) {
        ctx.set(ERROR_CODE_KEY, status.value());
        ctx.set(ERROR_MESSAGE_KEY, message);
        ctx.setSendZuulResponse(false);
    }
}
