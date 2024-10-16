const dgram = require('dgram');
const client = dgram.createSocket('udp4');

// 定义要发送的消息
const serverPort = 8080;
const serverAddress = '127.0.0.1';
const timeoutDuration = 2000; // 超时时间（毫秒）
const maxRetransmissions = 5; // 最大重传次数
let retransmissions = 0; // 当前重传次数
let timeout;

// 发送消息并处理超时重传逻辑
async function sendMessage(retransmissions, message) {
    client.send(message, serverPort, serverAddress, (err) => {
      if (err) {
        console.error('发送消息时出错:', err);
      } else {
        console.log(`第 ${retransmissions} 次消息已发送` + message);
      }
  
      // 设置超时计时器，如果在超时时间内没有收到响应，则重发消息
      timeout = setTimeout(() => {
        if (retransmissions < maxRetransmissions) {
          console.log('未收到响应，重发消息');
          sendMessage(retransmissions + 1, message); // 重新发送消息
        } else {
          console.log('超过最大重传次数，停止发送');
          client.close(); // 关闭客户端
        }
      }, timeoutDuration);
    });
  }

// 设置接收服务器响应的处理器
client.on('message', (msg, rinfo) => {
    console.log(`收到来自服务器的响应: \n${msg}`);
    clearTimeout(timeout); // 清除超时计时器
    yyy();
    //client.close(); // 关闭客户端
  });




// 提示用户输入并读取
async function yyy() {
  console.log('\n请选择一个功能：');
  console.log('1. 根据出发地和目的地查询航班');
  console.log('2. 根据航班 ID 查询航班');
  console.log('3. 根据航班 ID 预订座位');
  console.log('4. 订阅航班更新');
  console.log('5. 随机选择座位');
  console.log('6. 获取预订信息');
  console.log('7. 退出');

  const choice = await askQuestion('请输入你的选择（1-7）：');
  console.log(`您选择了: ${choice}\n`);

  if (parseInt(choice) === 1) {
    // 根据出发地和目的地查询航班
    const source = await askQuestion('请输入出发地: ');
    const destination = await askQuestion('请输入目的地: ');
    const message = `request=QueryFlightBySrcAndDes,source=${source},destination=${destination},semantic=at-least-once`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 2) {
    // 根据航班 ID 查询航班
    const id = await askQuestion('请输入航班 ID: ');
    const message = `request=QueryFlightById,id=${id},semantic=at-least-once`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 3) {
    // 根据航班 ID 预订座位
    const id = await askQuestion('请输入航班 ID: ');
    const seats = await askQuestion('请输入预订的座位数: ');
    const message = `request=MakeReservationById,id=${id},seats=${seats},semantic=at-least-once`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 4) {
    // 订阅航班更新
    const id = await askQuestion('请输入航班 ID: ');
    const timeinterval = await askQuestion('请输入时间间隔（分钟）: ');
    const message = `request=SubscribeById,id=${id},timeinterval=${parseInt(timeinterval) * 60000},semantic=at-least-once`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 5) {
    // 随机选择座位
    const id = await askQuestion('请输入航班 ID: ');
    const seats = await askQuestion('请输入预订的座位数: ');
    const message = `request=RandomChooseSeat,id=${id},seats=${seats},semantic=at-least-once`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 6) {
    // 获取预订信息
    const message = `request=GetBookingInfo,semantic=at-least-once`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 7) {
    // 退出程序
    console.log('感谢使用，程序已退出。');
    client.close();
    rl.close();
  } else {
    console.log('无效的选择，请重试。');
    yyy(); // 重新提示用户输入
  }
}

yyy();






// 启动发送消息
//sendMessage(1,Buffer.from('request=QueryFlightBySrcAndDes,source=New York,destination=London,semantic=at-least-once'));
//sendMessage(1,Buffer.from('request=bookFlight,source=New York,destination=London,semantic=at-least-once'));
//sendMessage(1,Buffer.from('request=QueryFlightById,id=101,semantic=at-least-once'));
//sendMessage(1,Buffer.from('request=MakeReservationById,id=110,seats=1,semantic=at-least-once'));
//sendMessage(1,Buffer.from('request=SubscribeById,id=110,timeinterval=150000,semantic=at-least-once'));