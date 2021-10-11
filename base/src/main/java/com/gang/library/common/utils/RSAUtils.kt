package com.gang.library.common.utils

import android.util.Base64
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils
 * @ClassName:      RSAUtils
 * @Description:    java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:29
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
private var publicKey: RSAPublicKey? = null
/**************************** RSA 公钥加密解密 */
/**
 * 从字符串中加载公钥,从服务端获取
 *
 * @param
 * @throws Exception 加载公钥时产生的异常
 */
fun loadPublicKey(pubKey: String?) {
    try {
        val buffer =
            Base64.decode(pubKey, Base64.DEFAULT)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec =
            X509EncodedKeySpec(buffer)
        publicKey =
            keyFactory.generatePublic(keySpec) as RSAPublicKey
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 公钥加密过程
 *
 * @param
 * @param plainData 明文数据
 * @return
 * @throws Exception 加密过程中的异常信息
 */
@Throws(Exception::class)
fun encryptWithRSA(plainData: String): String {
    if (publicKey == null) {
        throw NullPointerException("encrypt PublicKey is null !")
    }
    var cipher: Cipher? = null
    cipher =
        Cipher.getInstance("RSA/ECB/PKCS1Padding") // 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val output = cipher.doFinal(plainData.toByteArray(charset("utf-8")))
    // 必须先encode成 byte[]，再转成encodeToString，否则服务器解密会失败
// byte[] encode = Base64.encode(output, Base64.DEFAULT);
    return Base64.encodeToString(output, Base64.DEFAULT)
}

/**
 * 公钥解密过程
 *
 * @param
 * @param encryedData 明文数据
 * @return
 * @throws Exception 加密过程中的异常信息
 */
@Throws(Exception::class)
fun decryptWithRSA(encryedData: String?): String {
    if (publicKey == null) {
        throw NullPointerException("decrypt PublicKey is null !")
    }
    var cipher: Cipher? = null
    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding") // 此处如果写成"RSA"解析的数据前多出来些乱码
    cipher.init(Cipher.DECRYPT_MODE, publicKey)
    val output =
        cipher.doFinal(Base64.decode(encryedData, Base64.DEFAULT))
    return String(output)
}
/**************************** RSA 公钥加密解密 */