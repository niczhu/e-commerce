package org.seckill.web;

import org.seckill.model.Exposer;
import org.seckill.model.SeckillExecution;
import org.seckill.model.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuhp on 2016/12/26.
 *
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 秒杀列表页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"list","/"}, method = RequestMethod.GET)
    public String list(Model model) {
        logger.info("访问 /list ");

        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    /**
     * 秒杀商品详情页
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        logger.info("访问 /"+seckillId+"/detail ");

        if (seckillId == null) {
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getById(seckillId);

        if (seckill == null) {
            return "redirect:/seckill/list";
        }

        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     *
     * @param seckillId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SeckillResult<Exposer> exposer(@PathVariable String seckillId) {
        logger.info("访问 /"+seckillId+"/exposer ");

        SeckillResult<Exposer> result;
        try {

            Exposer exposer = seckillService.exportSeckillUrl(Long.valueOf(seckillId));
            result = new SeckillResult<Exposer>(true, exposer);

        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 执行秒杀
     *
     * @param seckillId
     * @param md5
     * @param phone
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        logger.info("访问 /"+seckillId+ "/"+md5+"/execution ");
        if(phone == null ){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }

        SeckillResult<SeckillExecution> result;

        try {

            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);

            return new SeckillResult<SeckillExecution>(true, execution);

        } catch (RepeatKillException e2){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,execution);

        } catch (SeckillCloseException e1){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(false,execution);

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }
    }

    /**
     * 获利系統时间
     */

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> sysTime(){
        logger.info("访问 /time/now ");
        Date now = new Date();
        return new SeckillResult(true,now.getTime());
    }

    /**
     * 开始秒杀活动
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "test",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public Object statSeckill(){
        //开始活动
        return seckillService.test("hello");
    }

}
