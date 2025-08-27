package org.example;

import com.alibaba.higress.sdk.model.Route;
import com.alibaba.higress.sdk.model.route.RoutePredicate;
import com.alibaba.higress.sdk.model.route.RoutePredicateTypeEnum;
import com.alibaba.higress.sdk.model.route.UpstreamService;

import java.io.IOException;
import java.util.Collections;

public class HigressMain {

    public static void main(String[] args) throws IOException {
        Route route = Route.builder()
                .name("")
                .path(RoutePredicate.builder()
                        .matchType(RoutePredicateTypeEnum.EQUAL.name())
                        .matchValue("/anything/post")
                        .build())
                .services(Collections.singletonList(
                        UpstreamService.builder()
                                .name("10.0.113.66:19989")
                                .build()
                )).build();
    }
}
