package com.yjw.backend.service;

import com.yjw.backend.utils.ResponseBuilder;

/**
 *
 * @author jackLiu
 * @since 2020-04-03
 */
public interface ISearchService {
    ResponseBuilder search(int model, int current, String value);
}
