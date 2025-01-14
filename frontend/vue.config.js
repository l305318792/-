/*
 * @Description: 
 * @Author: Rabbiter
 * @Date: 2023-01-14 20:02:29
 */
module.exports = {
    lintOnSave: false, // 关闭eslint校验
    devServer: {
        host: "localhost",
        port: 9283,
        https: false,
        proxy: "http://localhost:9281",
        client: {
            overlay: { // 关闭eslint校验
                warnings: false,
                errors: false
            }
        }
    }
}
//设置代理解决跨域问题