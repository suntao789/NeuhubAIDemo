package neuhub;


import com.fasterxml.jackson.databind.ObjectMapper;
import neuhub.properties.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link NeuhubAIDemoTester#asr()} 语音识别接口
 * {@link NeuhubAIDemoTester#comment()} 评论观点抽取接口
 * {@link NeuhubAIDemoTester#faceCompare()} 人脸对比接口
 * {@link NeuhubAIDemoTester#faceDetectAttr()} 人脸检测与属性分析接口
 * {@link NeuhubAIDemoTester#faceAntiSpoof()} 人脸活体检测接口
 * {@link NeuhubAIDemoTester#food()} 菜品识别接口
 * {@link NeuhubAIDemoTester#humanDetect()} 人体检测接口
 * {@link NeuhubAIDemoTester#idCard()} 身份证识别接口
 * {@link NeuhubAIDemoTester#invoice()} 增值税发票识别接口
 * {@link NeuhubAIDemoTester#leaderRec()} 特定人物识别接口
 * {@link NeuhubAIDemoTester#lexer()} 词法分析接口
 * {@link NeuhubAIDemoTester#poseEstimation()} 人体关键点检测接口
 * {@link NeuhubAIDemoTester#searchFace()} 人脸搜索接口
 * {@link NeuhubAIDemoTester#faceGroupCreate()} 创建人脸分组接口
 * {@link NeuhubAIDemoTester#faceGroupDelete()} 删除人脸分组接口
 * {@link NeuhubAIDemoTester#faceCreate()} 创建人脸接口
 * {@link NeuhubAIDemoTester#faceDelete()} 删除人脸接口
 * {@link NeuhubAIDemoTester#getFaceGroupList()} 获取人脸分组列表接口
 * {@link NeuhubAIDemoTester#selfieSegmentation()} 自拍人像抠图接口
 * {@link NeuhubAIDemoTester#sentiment()} 情感分析接口
 * {@link NeuhubAIDemoTester#sexyGet()} 智能鉴黄(GET请求)接口
 * {@link NeuhubAIDemoTester#sexyPost()} 智能鉴黄(POST请求)接口
 * {@link NeuhubAIDemoTester#similarity()} 短文本相似度接口
 * {@link NeuhubAIDemoTester#snapShop()} 拍照购接口
 * {@link NeuhubAIDemoTester#textClassification()} 文本分类接口
 * {@link NeuhubAIDemoTester#systax()} 句法分析接口
 * {@link NeuhubAIDemoTester#tts()} 语音合成接口
 * {@link NeuhubAIDemoTester#universal()} 通用文字识别接口
 * {@link NeuhubAIDemoTester#vehicle()} 行驶证识别接口
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeuhubAIDemoApplication.class)
public class NeuhubAIDemoTester {

    private Logger logger = LoggerFactory.getLogger(NeuhubAIDemoTester.class);
    private RestTemplate restTemplate;
    private ClientCredentialsResourceDetails clientCredentialsResourceDetails;

    /**
     * 这不是一个普通的RestTemplate，而是引用的OAuth2RestTemplate
     *
     * @param restTemplate
     */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Autowired
    public void setClientCredentialsResourceDetails(ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
        this.clientCredentialsResourceDetails = clientCredentialsResourceDetails;
    }

    /**
     * 调用的api网关地址
     */
    @Value("${gateway.url}")
    private String gatewayUrl;

    /**
     * 测试时使用的图片位置，在配置文件中进行修改
     */
    @Value("${neuhub.picture}")
    private String picture;

    /**
     * 人脸对比接口测试时使用的图片位置，在配置文件中进行修改
     */
    @Value("${neuhub.pictureCompare}")
    private String pictureCompare;

    /**
     * 测试时使用的文本内容，在配置文件中进行修改
     */
    @Value("${neuhub.comment}")
    private String comment;

    /**
     * 测试短文本相似度接口的文本内容，在配置文件中进行修改
     */
    @Value("${neuhub.commentCompare}")
    private String commentCompare;

    /**
     * 测试之前进行环境检查，确保输入正确的clientId和clientSecret
     */
    @Before
    public void checkEnvironment() {
        int clientIdLength = 32;
        int clientSecretLength = 10;
        if (clientCredentialsResourceDetails.getClientId() == null || clientCredentialsResourceDetails.getClientId().length() != clientIdLength) {
            logger.error("clientId有误，请重新填写");
            System.exit(1);
        }

        if (clientCredentialsResourceDetails.getClientSecret() == null || clientCredentialsResourceDetails.getClientSecret().length() != clientSecretLength) {
            logger.error("clientSecret有误，请重新填写");
            System.exit(1);
        }
    }

    @Test
    public void humanDetect() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        //以下参数仅为示例值
        String requestUrl = gatewayUrl + "/neuhub/human_detect";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void poseEstimation() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        //muti_det为单人姿态(1)或多人姿态(2)
        int muti_det = 1;
        String requestUrl = gatewayUrl + "/neuhub/pose_estimation?muti_det={muti_det}";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class, muti_det);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceAntiSpoof() {
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        String value = String.format("imageBase64=%s", encodedText);
        HttpEntity<String> requestEntity = new HttpEntity<>(value);
        String requestUrl = gatewayUrl + "/neuhub/face_AntiSpoof";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceCompare() {
        byte[] face1Data = dataBinary(picture);
        byte[] face2Data = dataBinary(pictureCompare);
        String face1 = imageBase64(face1Data);
        String face2 = imageBase64(face2Data);
        String param = String.format("imageBase64_1=%s&imageBase64_2=%s", face1, face2);
        HttpEntity<String> requestEntity = new HttpEntity<>(param);
        String requestUrl = gatewayUrl + "/neuhub/face_compare";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceDetectAttr() {
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        String value = String.format("imageBase64=%s", encodedText);
        HttpEntity<String> requestEntity = new HttpEntity<>(value);
        String requestUrl = gatewayUrl + "/neuhub/face_detect_attr";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceGroupCreate() {
        //groupName为分组名称
        /**
         * groupName为用户创建分组的名称，根据分组名称完成创建后可以获得groupId，
         * 通过{@link NeuhubAIDemoTester#getFaceGroupList()} 查看分组信息
         */
        String groupName = "test0726";
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, null);
        String requestUrl = gatewayUrl + "/neuhub/faceGroupCreate?groupName={groupName}";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class, groupName);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceCreate() {
        //groupId为创建的人脸分组ID，outerId为待创建的人脸ID
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        String param = String.format("imageBase64=%s", encodedText);
        HttpEntity<Object> requestEntity = new HttpEntity<>(param);
        /**
         *  groupId需要调接口去创建
         *  {@link NeuhubAIDemoTester#faceGroupCreate()}
         */
        String groupId = "bfaca672-d954-4207-8e35-26aeeb276d71";
        /**
         * 人脸图片的id值，用户自己生成，自己控制去重
         */
        String outerId = "0726testFace1";
        String requestUrl = gatewayUrl + "/neuhub/face_create?groupId={groupId}&outerId={outerId}";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class, groupId, outerId);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceDelete() {
        /**
         * groupId  需要调接口去创建 {@link NeuhubAIDemoTester#faceGroupCreate()}
         * outerId  待删除的人脸图片的id值
         */
        String groupId = "bfaca672-d954-4207-8e35-26aeeb276d71";
        String outerId = "0726testFace1";
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, null);
        String requestUrl = gatewayUrl + "/neuhub/faceDelete?groupId=bfaca672-d954-4207-8e35-26aeeb276d71&groupName=&outerId=0726testFace1";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void faceGroupDelete() {
        /**
         * groupId 为待删除的分组ID
         */
        String groupId = "5249f5d4-96ad-46b5-8e88-1e51be2d20c8";
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, null);
        String requestUrl = gatewayUrl + "/neuhub/faceGroupDelete?groupId={groupId}";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class, groupId);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void getFaceGroupList() {
        /**
         * start为查询起始位置，length为从查询起始位置开始查询的长度
         */
        int start = 0;
        int length = 5;
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, null);
        String requestUrl = gatewayUrl + "/neuhub/getFaceGroupList?start={start}&length={length}";
        Map<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("start", start);
        urlVariables.put("length", length);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class, urlVariables);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void searchFace() {
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        String param = String.format("imageBase64=%s", encodedText);
        HttpEntity<Object> requestEntity = new HttpEntity<>(param);
        //groupId为分组ID
        String groupId = "5249f5d4-96ad-46b5-8e88-1e51be2d20c8";
        String requestUrl = gatewayUrl + "/neuhub/faceSearch?groupId={groupId}";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class, groupId);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void sexyGet() {
        //image_url后为待测试的图片的路径
        String requestUrl = gatewayUrl + "/neuhub/cvImage?image_url=http://img.mp.itc.cn/upload/20170109/67793cdf254848d8a0e59a4f6e034534_th.jpg";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(requestUrl, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void sexyPost() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        String requestUrl = gatewayUrl + "/neuhub/localCvImage";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void food() throws Exception {
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        // Picture类为自创的实体类
        Picture pic = new Picture(encodedText);
        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(pic);
        HttpEntity<String> requestEntity = new HttpEntity<>(value);
        String requestUrl = gatewayUrl + "/neuhub/FoodApi";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void leaderRec() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        //以下参数仅为示例值
        String requestUrl = gatewayUrl + "/neuhub/PoliticiansRecognition";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void selfieSegmentation() throws Exception {
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        // Picture类为自创的实体类
        Picture pic = new Picture(encodedText);
        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(pic);
        HttpEntity<String> requestEntity = new HttpEntity<>(value);
        String requestUrl = gatewayUrl + "/neuhub/SelfieSeg";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void snapShop() {
        byte[] data = dataBinary(picture);
        String encodedText = imageBase64(data);
        //以下参数仅为示例值
        //todo channel_id 为test 需向管理员申请一个专属的
        String channelId = "test";
        String request = String.format("channel_id=%s&&imgBase64=%s", channelId, encodedText);
        HttpEntity<String> requestEntity = new HttpEntity<>(request);
        String requestUrl = gatewayUrl + "/neuhub/snapshop";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void comment() {
        Map<String, String> map = new HashMap<>();
        map.put("text", comment);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map);
        String requestUrl = gatewayUrl + "/neuhub/CommentTag";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void lexer() {
        int type = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("text", comment);
        map.put("type", type);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(map);
        String requestUrl = gatewayUrl + "/neuhub/lexer";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void sentiment() {
        //type为情感模型的类型
        int type = 1;
        //body请求参数
        Sentiment sentiment = new Sentiment(type, comment);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sentiment> requestEntity = new HttpEntity<>(sentiment, httpHeaders);
        String requestUrl = gatewayUrl + "/neuhub/sentiment";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void similarity() {
        //body请求参数
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("text1", comment);
        postParameters.put("text2", commentCompare);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(postParameters);
        String requestUrl = gatewayUrl + "/neuhub/similarity";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void systax() {
        Map<String, String> map = new HashMap<>();
        map.put("text", comment);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map);
        String requestUrl = gatewayUrl + "/neuhub/parser";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void textClassification() {
        Map<String, String> map = new HashMap<>();
        map.put("text", comment);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map);
        String requestUrl = gatewayUrl + "/neuhub/textClassification";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void idCard() {
        //需要传一张真实的身份证的照片
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        String requestUrl = gatewayUrl + "/neuhub/ocr_idcard";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void invoice() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        String requestUrl = gatewayUrl + "/neuhub/ocr_invoice";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void universal() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        String requestUrl = gatewayUrl + "/neuhub/ocr_universal";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    @Test
    public void vehicle() {
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        String requestUrl = gatewayUrl + "/neuhub/ocr_vehicle_recognition";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    /**
     * domain为语音识别领域，其他参数值请参考文档，applicationId为产品ID，requestId为请求语音串标识码，
     * sequenceId为语音分段传输的分段号，asrProtocol为通信协议版本号，netState为
     * 客户端网络状态，applicator为应用者，AsrEncode构造函数后参数分别为音频声道
     * 数、音频格式和采样率，AsrProperty构造函数后参数为是否开启服务端自动断尾、
     * 语音识别的请求格式AsrEncode、各平台的机型信息和客户端版本号
     */
    @Test
    public void asr() {
        //header示例参数
        String domain = "search";
        String applicationId = "search-app";
        String requestId = "56a847e6-84c0-4c01-bf4b-d566f2d2dd11-app";
        int sequenceId = -1;
        int asrProtocol = 1;
        int netState = 2;
        int applicator = 1;
        byte[] data = dataBinary(picture);
        AsrEncode asrEncode = new AsrEncode(1, "wav", 16000);
        AsrProperty property = new AsrProperty(false, asrEncode, "Linux", "0.0.0.1");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Domain", domain);
        httpHeaders.set("Application-Id", applicationId);
        httpHeaders.set("Request-Id", requestId);
        httpHeaders.set("Sequence-Id", Integer.toString(sequenceId));
        httpHeaders.set("Asr-Protocol", Integer.toString(asrProtocol));
        httpHeaders.set("Net-State", Integer.toString(netState));
        httpHeaders.set("Applicator", Integer.toString(applicator));
        httpHeaders.set("property", property.toString());
        HttpEntity<Object> requestEntity = new HttpEntity<>(data, httpHeaders);
        //以下参数仅为示例值
        String requestUrl = gatewayUrl + "/neuhub/asr";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    /**
     * serviceType为服务类型，requestId为请求语音串标识码，sequenceId为文本分段传输的分段号，
     * protocol为通信协议版本号，netState为客户端网络状态，applicator为应用者，TtsParameters
     * 构造函数后内容依次为文本编码格式 、音频编码格式、音色、音量、语速和采样率，TtsProperty构
     * 造函数后内容依次为各平台的机型信息、客户端版本号和TtsParameters
     */
    @Test
    public void tts() {
        String serviceType = "synthesis";
        String requestId = "65845428-de85-11e8-9517-040973d59a1e";
        int sequenceId = 1;
        int protocol = 1;
        int netState = 1;
        int applicator = 1;
        TtsParameters ttsParameters = new TtsParameters("1", "1", "0", "2.0", "1.0", "24000");
        TtsProperty property = new TtsProperty("Linux", "0.0.0.1", ttsParameters);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Service-Type", serviceType);
        httpHeaders.set("Request-Id", requestId);
        httpHeaders.set("Sequence-Id", Integer.toString(sequenceId));
        httpHeaders.set("Protocol", Integer.toString(protocol));
        httpHeaders.set("Net-State", Integer.toString(netState));
        httpHeaders.set("Applicator", Integer.toString(applicator));
        httpHeaders.set("property", property.toString());
        HttpEntity<String> requestEntity = new HttpEntity<>(comment, httpHeaders);
        String requestUrl = gatewayUrl + "/neuhub/tts";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        result(responseEntity);
    }

    private String imageBase64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    private byte[] dataBinary(String addr) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(addr));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private void result(ResponseEntity<String> responseEntity) {
        String responseBody = responseEntity.getBody();
        logger.info("调用结果: {}", responseBody);
    }
}


