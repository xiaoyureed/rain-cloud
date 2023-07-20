package io.github.xiaoyureed.raincloud.core.starter.database.x;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import io.github.xiaoyureed.raincloud.core.starter.database.model.AbstractBaseEntity;
import io.github.xiaoyureed.raincloud.core.starter.database.util.MyBatisUtils;
import io.github.xiaoyureed.raincloud.core.starter.database.util.WrapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;

/**
 * xiaoyureed@gmail.com
 */
public abstract class AbstractBaseController<
    S extends AbstractBaseServiceX<M, T>,
    M extends IBaseMapperX<T>,
    T extends AbstractBaseEntity
    > {

    @Autowired
    private S baseService;

    @PostMapping("page")
    @Operation(summary = "find all with pagination")
    public ResponseEntity<ResponseWrapper<List<T>>> list(@RequestBody @Nullable T req) throws Throwable {
        Page<T> page;
        if (req == null) {
            page = baseService.page(MyBatisUtils.buildPage(ServletUtils.page()));
        } else {
            page = baseService.page(
                MyBatisUtils.buildPage(ServletUtils.page()),
                WrapperUtil.parse(req, baseService.getEntityClass())
            );
        }

        List<T> result = page.getRecords();

        return ResponseEntity.ok(ResponseWrapper.ok(result));
    }

    @GetMapping("{id}")
    @Operation(summary = "get by id")
    public ResponseEntity<ResponseWrapper<T>> getById(
        @PathVariable("id") @Parameter(description = "primary key", required = true) String id
    ) throws Throwable {
        return ResponseEntity.ok(ResponseWrapper.ok(baseService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "save or update single one")
    public ResponseEntity<ResponseWrapper<String>> save(@RequestBody T req) throws Throwable {
        baseService.saveOrUpdate(req);
        return ResponseEntity.ok(ResponseWrapper.ok(req.getId()));
    }

    @DeleteMapping
    @Operation(summary = "delete by ids")
    public ResponseEntity<ResponseWrapper<?>> deleteByIds(
        @Parameter(description = "primary keys", required = true) @RequestParam("ids") String[] ids
    ) throws Throwable {
//        List<String> collect = Arrays.stream(ids.split(",")).map(String::trim).collect(Collectors.toList());
        baseService.removeBatchByIds(Arrays.stream(ids).toList());
        return ResponseEntity.ok(ResponseWrapper.ok());
    }

}
