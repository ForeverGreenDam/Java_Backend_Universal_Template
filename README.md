# Java_Backend_Universal_Template

一个通用的 Java 后端初始化模板，基于 Spring Boot 和经典 MVC 架构，包含常用中间件配置、基础用户认证与示例集成（JWT、MyBatis、Redis、微信企业号消息发送等），方便快速搭建微服务或内部管理系统的骨架工程。

## 主要特性
- **用户模块**：注册、登录（JWT）、获取当前用户信息、更新/编辑/删除用户的示例实现。  
- **持久层**：MyBatis Mapper 示例与 XML 映射（含动态更新/插入示例）。  
- **鉴权**：基于 JWT 的拦截器（`JwtTokenInterceptor`）和线程上下文 `BaseContext`。  
- **微信企业号工具**：封装了发送多种消息类型（文本、图片、语音、视频、文件、图文、Markdown、文本卡片）的工具类 `WechatUtils`。  
- **密码处理**：引入 bcrypt 风格的密码工具（含项目内回退实现），建议在生产环境引入 `spring-security-crypto` 或 `jBCrypt`。  
- **测试**：包含部分单元测试（Mockito + JUnit）示例，和用于触发微信消息发送的测试接口 `TestController`。

## 快速开始
1. 克隆仓库：

   ```bash
   git clone <repo-url>
   cd Java_Backend_Universal_Template
   ```

2. 配置应用（`src/main/resources/application.yml` / `application-dev.yml`）：
   - 配置数据库连接、MyBatis 数据源
   - 配置 Redis（用于缓存微信 access_token 示例）
   - 配置 `wechat` 属性（`WechatProperties`）以启用微信消息发送
   - 配置 JWT 密钥（`JwtProperties`）

3. 构建并运行：

   ```bash
   mvn clean package
   java -jar target/*.jar
   ```

4. 运行测试：

   ```bash
   mvn test
   ```

## 常用端点（示例）
- `POST /user/register` — 用户注册（示例 DTO：`UserRegisterDTO`）  
- `POST /user/login` — 用户登录，返回 JWT（示例 DTO：`UserLoginDTO`）  
- `GET /user/getInfo` — 获取当前用户信息（基于拦截器与 ThreadLocal）  
- `GET /test/wechat/*` — 一组测试接口，用于触发微信消息发送（见 `TestController`）

## 代码组织（主要包）
- `controller` — HTTP 控制器（REST API）  
- `service` / `service.impl` — 业务逻辑接口与实现  
- `mapper` — MyBatis Mapper 接口与 XML 映射（`src/main/resources/mapper`）  
- `model.dto` / `model.vo` / `model.entity` — DTO / VO / 实体类  
- `common.utils` — 工具类（如 `WechatUtils`、`PasswordUtils`、`JwtUtil` 等）  

## 注意事项与建议
- 当前项目内提供了一个简化的 bcrypt-like 回退实现以保证自包含，可在生产环境替换为 `BCryptPasswordEncoder`（推荐通过 `org.springframework.security:spring-security-crypto`）或 `org.mindrot:jbcrypt`。  
- 请务必在生产环境使用安全的 JWT 密钥和更强的密码策略（例如 bcrypt 强度 >= 10）。  
- 微信企业号发送在本项目中使用 Redis 缓存 `access_token`，需要配置 Redis 才能正常工作。  

## 贡献与作者
作者：ForeverGreenDam  
欢迎提交 issue 或 PR 来改进模板功能与示例。

## 微信临时素材上传（新增）

本模板新增了企业微信临时素材上传的完整实现（含数据库记录），相关文件：
- 工具类（仅负责对接微信 API）: `src/main/java/com/greendam/template/common/utils/WechatUtils.java`
- 服务层（负责业务逻辑与持久化）: `src/main/java/com/greendam/template/service/FileService.java` 和 `src/main/java/com/greendam/template/service/impl/FileServiceImpl.java`
- 数据库表：`temp_file`（见 `sql/createtable.sql`）
- 实体：`src/main/java/com/greendam/template/model/entity/TempFile.java`
- Mapper：`src/main/java/com/greendam/template/mapper/TempFileMapper.java` 与 `src/main/resources/mapper/TempFileMapper.xml`
- 微信 API 响应实体：`src/main/java/com/greendam/template/common/entity/wechat/response/UploadMediaResponse.java`

功能点与使用说明：
- 上传接口会先通过 `WechatUtils` 调用企业微信 `media/upload` 接口获取 `media_id`，成功后由 `FileServiceImpl` 将 `media_id`、文件名、大小、类型和上传时间写入 `temp_file` 表。
- 支持通过 `MultipartFile` 直接上传（控制器示例：`TestController.uploadFile`），也支持按 UTF-8 编码上传纯文本内容（`FileService.uploadWechatTempTextFile`）。
- `media_id` 在企业微信中**仅三天有效**，请在业务中注意过期处理或按需重新上传。

示例（Controller 调用示例）：

```java
@PostMapping("/wechat/upload")
public BaseResponse<String> uploadFile(MultipartFile file, String type) {
    String mediaId = fileService.uploadWechatTempFile(file, type);
    return BaseResponse.success(mediaId);
}
```

配置与注意：
- 请在 `application.yml` 中配置 `your.wechat` 前缀下的 `corpid`、`secret`、`agentId`（对应 `WechatProperties`）。
- 需要启用并配置 Redis（用于缓存 access_token），示例配置见 `src/main/resources/application.yml`。
- 数据库中已包含 `temp_file` 建表语句（`sql/createtable.sql`），请在部署前执行建表。

调试与日志：
- 上传失败时会抛出 `BusinessException`，请查看服务日志以获取微信返回的错误码与信息（例如 `empty media data` 或 `44001` 等）。
- 若遇到微信接口返回格式或上传失败的问题，可检查 HTTP 请求的 multipart 边界、Content-Disposition 中 `filename` 与 `filelength` 字段是否正确。