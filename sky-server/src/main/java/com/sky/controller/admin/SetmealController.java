package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询:{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 新增套餐
     * @param
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache",key="#setmealDTO.categoryId")//key:setmealCache::100
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.setWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐:{}",ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("根据id查询套餐:{}",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐
     */
    @PutMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody  SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用套餐:{},{}",status,id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
