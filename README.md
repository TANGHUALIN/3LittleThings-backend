# 3LittleThings-backend
www.3littlethings.top  

# バックエンド部分<br>
言語:Java EE(Servlet) jdk11<br>
アプリサーバー：Tomcat9<br>
Webサーバ:Nginx<br>
RDBMS:MySql<br>
JDBC接続プール：alibaba Druid<br>
クラウドサーバ：alibaba Cloud centOS7.9<br>
ビルドツール：Maven<br>
IDE:IntelliJ IDEA<br>
SSL証明書：Let’s Encrypt <br>

重要 :exclamation:  
ローカル開発とクラウド上のフロントエンドとバックエンドの通信方法が違います。 <br>
ソースコードはクラウド上と同じです。 <br>
ローカルでは、ViteからTomcatにリクエストを送ります。CORSの対策として、CorsFilter.javaを追加し、web.xmlでfilterを設定します。  <br>
クラウド上では、Nginxは静的なコンテンツを処理し、動的なコンテンツはTomcatに転送されます。<br>

Gmail APIの使い方: <br>
Googleのサービスアカウントがお金をかけなければならないらしいので、今回は手間のかかる方法を使います。<br>
目標: 3littlethingsdiary@gmail.comから自動的にユーザーに検証メールを送ります。<br>
まず、このメールアドレスの許可を得る必要があります。 <br>
Google OAuth2.0に対して、2つのファイルが必要です。 <br>
1つ目: https://console.cloud.google.com/apis/ を通してcredentials.jsonを取得します。このファイルはアプリケーションとGoogle APIの間で通信するために必要です。 <br>
2つ目: コードを実行して、Google OAuth 2.0の画面で3littlethingsdiary@gmail.comにログインして許可を与えます。そうすると、storedTokenが自動的に生成されます。このTokenが有効期限内であれば、自動的にメールを送ることができます。 <br>

SSL証明書: <br>
Linuxのターミナルで取得してNginxに配置します。<br>
