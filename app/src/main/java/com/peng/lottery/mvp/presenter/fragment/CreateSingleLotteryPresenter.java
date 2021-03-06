package com.peng.lottery.mvp.presenter.fragment;

import com.peng.lottery.app.helper.LotteryHelper;
import com.peng.lottery.app.type.LotteryType;
import com.peng.lottery.base.BasePresenter;
import com.peng.lottery.mvp.model.DataManager;
import com.peng.lottery.mvp.model.db.bean.LotteryNumber;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.peng.lottery.app.type.LotteryType.LOTTERY_TYPE_11X5;
import static com.peng.lottery.app.type.LotteryType.LOTTERY_TYPE_DLT;
import static com.peng.lottery.app.type.NumberBallType.NUMBER_BALL_TYPE_BLUE;
import static com.peng.lottery.app.type.NumberBallType.NUMBER_BALL_TYPE_OTHER;
import static com.peng.lottery.app.type.NumberBallType.NUMBER_BALL_TYPE_RED;

public class CreateSingleLotteryPresenter extends BasePresenter {

    private LotteryHelper mHelper;

    @Inject
    public CreateSingleLotteryPresenter(DataManager dataManager) {
        super(dataManager);

        mHelper = LotteryHelper.getInstance();
    }

    /**
     * 检测是否可以继续添加选择的号码
     */
    public boolean checkIsAdd(List<LotteryNumber> lotteryNumbers, LotteryType lotteryType, LotteryNumber number) {
        if (lotteryNumbers.contains(number)) {
            return false;
        }
        if (NUMBER_BALL_TYPE_RED.type.equals(number.getNumberType()) || NUMBER_BALL_TYPE_BLUE.type.equals(number.getNumberType())) {
            // 大乐透和双色球
            int count = getNumberCount(lotteryNumbers, number.getNumberType());
            return NUMBER_BALL_TYPE_RED.type.equals(number.getNumberType())
                    ? LOTTERY_TYPE_DLT.equals(lotteryType) ? count < 5 : count < 6
                    : LOTTERY_TYPE_DLT.equals(lotteryType) ? count < 2 : count < 1;
        } else if (NUMBER_BALL_TYPE_OTHER.type.equals(number.getNumberType())) {
            // 11选5
            return lotteryNumbers.size() < mHelper.get11x5Size();
        } else {
            return false;
        }
    }

    /**
     * 排序当前选择好的彩票号码
     */
    public void sortList(List<LotteryNumber> lotteryNumbers, LotteryType lotteryType) {
        if (mHelper.checkLotterySize(lotteryNumbers, lotteryType)) {
            if (LOTTERY_TYPE_11X5.equals(lotteryType)) {
                if (mHelper.get11x5Sort()) {
                    Collections.sort(lotteryNumbers);
                }
            } else {
                Collections.sort(lotteryNumbers);
            }
        }
    }

    private int getNumberCount(List<LotteryNumber> lotteryNumbers, String numberType) {
        int count = 0;
        for (LotteryNumber lotteryNumber : lotteryNumbers) {
            if (lotteryNumber.getNumberType().equals(numberType)) {
                count++;
            }
        }
        return count;
    }
}
