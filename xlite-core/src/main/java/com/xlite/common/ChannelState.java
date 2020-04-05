package com.xlite.common;

/**
 * <p>
 * channel state
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public enum ChannelState {

    /**
     * 未初始化
     */
    UN_INIT(0),

    /**
     * 初始化
     */
    INIT(1),

    /**
     * 存活可用
     */
    ALIVE(2),

    /**
     * 不存活可用
     */
    UN_ALIVE(3),

    /**
     * 关闭
     */
    CLOSE(4);


    public int value;

    ChannelState(int value){
        this.value = value;
    }

    /**
     * 是否存活可用
     * @return
     */
    public boolean isAliveState(){
        return this == ALIVE;
    }

    /**
     * is closed
     * @return
     */
    public boolean isCloseState(){
        return this == CLOSE;
    }

    /**
     * is init
     * @return
     */
    public boolean isInitState(){
        return this == INIT;
    }

}
