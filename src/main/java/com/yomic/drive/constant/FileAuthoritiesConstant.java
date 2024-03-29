package com.yomic.drive.constant;

public class FileAuthoritiesConstant {
    public static final Long NEW            = 0b1L;     // 新建
    public static final Long UPDATE         = 0b10L;    // 修改
    public static final Long RENAME         = 0b100L;    //重命名
    public static final Long DELETE         = 0b1000L;    // 删除
    public static final Long PREVIEW        = 0b10000L;    // 预览
    public static final Long DOWNLOAD       = 0b100000L;    // 下载
    public static final Long LINK           = 0b1000000L;    // 外链

    public static boolean hasAuthorities(Long authorities, Long... bits) {
        Long sum = 0L;
        for(int i = 0; i < bits.length; i++) {
            sum = sum | bits[i];
        }
        return (sum & authorities) == sum;
    }
}
