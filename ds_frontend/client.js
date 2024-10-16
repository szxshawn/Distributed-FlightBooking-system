const dgram = require('dgram');
const client = dgram.createSocket('udp4');
const readline = require('readline');

// Define server information
const serverPort = 8080;
const serverAddress = '127.0.0.1';
const timeoutDuration = 2000; // Timeout duration (milliseconds)
const maxRetransmissions = 5; // Maximum number of retransmissions
let timeout;

// Create readline interface
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Send message and handle timeout retransmission logic
function sendMessage(retransmissions, message) {
  client.send(message, serverPort, serverAddress, (err) => {
    if (err) {
      console.error('Error sending message:', err);
    } else {
      console.log(`Message sent, attempt ${retransmissions}`);
    }

    // Set timeout. If no response is received within the timeout period, resend the message
    timeout = setTimeout(() => {
      if (retransmissions < maxRetransmissions) {
        console.log('No response received, resending message');
        sendMessage(retransmissions + 1, message); // Resend the message
      } else {
        console.log('Max retransmissions exceeded, stopping');
        client.close(); // Close client
        rl.close(); // Close readline interface
      }
    }, timeoutDuration);
  });
}

// Set handler for receiving server responses
client.on('message', (msg, rinfo) => {
  console.log(`\nResponse received from server:\n${msg}`);
  clearTimeout(timeout); // Clear timeout
  yyy(); // Prompt user input again
});

// Wrap readline.question as a Promise for use with async/await
function askQuestion(query) {
  return new Promise((resolve) => {
    rl.question(query, (answer) => {
      resolve(answer);
    });
  });
}

// Prompt user for input and read
async function yyy() {
  console.log('\nPlease select an option:');
  console.log('1. Query flights by source and destination');
  console.log('2. Query flight by ID');
  console.log('3. Reserve seats by flight ID');
  console.log('4. Subscribe to flight updates');
  console.log('5. Randomly select seats');
  console.log('6. Get booking information');
  console.log('7. Exit');

  const choice = await askQuestion('Enter your choice (1-7): ');
  console.log(`You selected: ${choice}\n`);

  if (parseInt(choice) === 1) {
    // Query flights by source and destination
    const source = await askQuestion('Enter source: ');
    const destination = await askQuestion('Enter destination: ');
    const flag = await askQuestion('Enter invocation semantics: 0. At most once 1. At least once');
    let semantic;
    if(parseInt(flag) === 0){
      semantic = 'at-most-once';
    }else if(parseInt(flag) === 1){
      semantic = 'at-most-once';
    }else{
      semantic = 'undefined';
    }
    const message = `request=QueryFlightBySrcAndDes,source=${source},destination=${destination},semantic=${semantic}`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 2) {
    // Query flight by ID
    const id = await askQuestion('Enter flight ID: ');
    const flag = await askQuestion('Enter invocation semantics: 0. At most once 1. At least once');
    let semantic;
    if(parseInt(flag) === 0){
      semantic = 'at-most-once';
    }else if(parseInt(flag) === 1){
      semantic = 'at-most-once';
    }else{
      semantic = 'undefined';
    }
    const message = `request=QueryFlightById,id=${id},semantic=${semantic}`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 3) {
    // Reserve seats by flight ID
    const id = await askQuestion('Enter flight ID: ');
    const flag = await askQuestion('Enter invocation semantics: 0. At most once 1. At least once ');
    const seats = await askQuestion('Enter number of seats to reserve: ');
    let semantic;
    if(parseInt(flag) === 0){
      semantic = 'at-most-once';
    }else if(parseInt(flag) === 1){
      semantic = 'at-most-once';
    }else{
      semantic = 'undefined';
    }
    const message = `request=MakeReservationById,id=${id},seats=${seats},semantic=${semantic}`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 4) {
    // Subscribe to flight updates
    const id = await askQuestion('Enter flight ID: ');
    const timeinterval = await askQuestion('Enter time interval (minutes): ');
    const flag = await askQuestion('Enter invocation semantics: 0. At most once 1. At least once');
    let semantic;
    if(parseInt(flag) === 0){
      semantic = 'at-most-once';
    }else if(parseInt(flag) === 1){
      semantic = 'at-most-once';
    }else{
      semantic = 'undefined';
    }
    const message = `request=SubscribeById,id=${id},timeinterval=${parseInt(timeinterval) * 60000},semantic=${semantic}`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 5) {
    // Randomly select seats
    const id = await askQuestion('Enter flight ID: ');
    const bookId = await askQuestion('Enter booking ID: ');
    const flag = await askQuestion('Enter invocation semantics: 0. At most once 1. At least once');
    let semantic;
    if(parseInt(flag) === 0){
      semantic = 'at-most-once';
    }else if(parseInt(flag) === 1){
      semantic = 'at-most-once';
    }else{
      semantic = 'undefined';
    }
    const message = `request=RandomChooseSeat,id=${id},bookid=${bookId},semantic=${semantic}`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 6) {
    const flag = await askQuestion('Enter invocation semantics: 0. At most once 1. At least once');
    let semantic;
    if(parseInt(flag) === 0){
      semantic = 'at-most-once';
    }else if(parseInt(flag) === 1){
      semantic = 'at-most-once';
    }else{
      semantic = 'undefined';
    }
    // Get booking information
    const message = `request=GetBookingInfo,semantic=${semantic}`;
    sendMessage(1, Buffer.from(message));
  } else if (parseInt(choice) === 7) {
    // Exit program
    console.log('Thank you for using, the program has exited.');
    client.close();
    rl.close();
  } else {
    console.log('Invalid choice, please try again.');
    yyy(); // Prompt user input again
  }
}

// Start the program
yyy();
