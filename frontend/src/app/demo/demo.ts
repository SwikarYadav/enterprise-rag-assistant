import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
declare var webkitSpeechRecognition: any;
// import { ViewChild, ElementRef } from '@angular/core';

import { ViewChild, ElementRef, NgZone } from '@angular/core';

@Component({
  selector: 'app-demo',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './demo.html',
  styleUrl: './demo.css'
})
    export class Demo {

      @ViewChild('chatBody') chatBody!: ElementRef;

  isChatOpen = false;

  userMessage = '';

  messages:any[] = [];

  apiUrl = 'http://13.206.249.42:8080/rag';

  recognition: any;
isListening: boolean = false;
showMenu = false;
isClosing = false;
showHelpMessage = false;
selectedFile!: File;
conversationId = this.generateConversationId(); 

constructor(private http: HttpClient, private ngZone: NgZone){}

generateConversationId(): string {

  return 'xxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
    .replace(/[xy]/g, function (c) {

      const r = Math.random() * 16 | 0;
      const v = c === 'x'
        ? r
        : (r & 0x3 | 0x8);

      return v.toString(16);

    });

}

  ngOnInit(){

  // wait for bounce animation to finish
  setTimeout(() => {

    this.showHelpMessage = true;

  }, 4000); // bounce time + 1 second delay

}

onFileSelected(event: any) {

  this.selectedFile = event.target.files[0];

  if (!this.selectedFile) {
    return;
  }

  this.uploadFile();

}
uploadFile() {

  const formData = new FormData();

  formData.append(
    'file',
    this.selectedFile
  );

  this.http.post(
    'http://13.206.249.42:8080/upload',
    formData,
    {
      responseType: 'text'
    }
  )
  .subscribe({

    next: () => {

      this.messages.push({
        text:
          `📄 ${this.selectedFile.name} uploaded successfully`,
        sender: 'bot'
      });

      this.scrollToBottom();

    },

    error: () => {

      this.messages.push({
        text:
          '❌ File upload failed',
        sender: 'bot'
      });

    }

  });

}



  scrollToBottom(){

  setTimeout(() => {

    if(this.chatBody){

      this.chatBody.nativeElement.scrollTop =
        this.chatBody.nativeElement.scrollHeight;

    }

  },50);

}

  showWelcomeMessage(){

  const welcome = {
    text: '',
    sender: 'bot',
    welcome: true 
  };

  this.messages.push(welcome);

  const text = "Hello! 👋 How can I help you today?";

  this.typeMessage(welcome, text);

}



  toggleMenu(){
  this.showMenu = !this.showMenu;
}

newChat() {

  this.messages = [];

  this.conversationId =
    this.generateConversationId();


  this.showMenu = false;

  this.showWelcomeMessage();
}

searchChat(){

  alert("Search chat feature coming soon");

  this.showMenu = false;

}

  toggleVoiceInput(){

  // if already listening → stop
  if(this.isListening){
    this.recognition.stop();
    this.isListening = false;
    return;
  }

  // create recognition instance
  this.recognition = new webkitSpeechRecognition();

  this.recognition.lang = 'en-IN';
  this.recognition.continuous = true;
  this.recognition.interimResults = true;

  this.recognition.start();

  this.isListening = true;

  this.recognition.onstart = () => {
    console.log("Voice recognition started...");
  };

  this.recognition.onresult = (event: any) => {

  let transcript = '';

  for (let i = event.resultIndex; i < event.results.length; i++) {
    transcript += event.results[i][0].transcript;
  }

  // force Angular UI update
  this.ngZone.run(() => {
    this.userMessage = transcript;
  });

};

  this.recognition.onerror = (event:any) => {
    console.error("Speech recognition error:", event.error);
  };

  this.recognition.onend = () => {

    this.isListening = false;

    console.log("Voice recognition stopped.");

  };

}
 toggleChat(){

  if(this.isChatOpen){

    // start closing animation
    this.isClosing = true;

    setTimeout(()=>{
      this.isChatOpen = false;
      this.isClosing = false;
    },300); // match animation duration

  }else{

    this.isChatOpen = true;

    if(this.messages.length === 0){
      this.showWelcomeMessage();
    }

  }

}




  sendMessage(){

    if(!this.userMessage.trim()) return;

    // USER MESSAGE
    this.messages.push({
      text: this.userMessage,
      sender: 'user'
    });

    this.scrollToBottom();
    
    const question = this.userMessage;

    this.userMessage = '';

    // BOT LOADER MESSAGE
    const loaderMessage = {
      sender: 'bot',
      loading: true
    };

    this.messages.push(loaderMessage);
    this.scrollToBottom();


    // API CALL
    this.http.post<any>(
  this.apiUrl,
  {
    conversationId: this.conversationId,
    question: question
  }
)
    .subscribe({

      next: (res) => {

        // remove loader
        this.messages.pop();

        const fullText = res.answer || "Sorry, I don't understand.";

        // empty bot message (for typing effect)
        const botMessage = {
          text: '',
          sender: 'bot'
        };

        this.messages.push(botMessage);

        // start typing animation
        this.typeMessage(botMessage, fullText);

      },

      error: () => {

        // remove loader
        this.messages.pop();

        const errorText = "⚠️ Server is temporarily unavailable. Please try again later.";

        const botMessage = {
          text: '',
          sender: 'bot'
        };

        this.messages.push(botMessage);

        this.typeMessage(botMessage, errorText);

      }

    });

  }



  // LETTER BY LETTER TYPING FUNCTION
  typeMessage(message:any, text:string){

    let i = 0;

    const typingSpeed = 20; // smaller = faster typing

    const interval = setInterval(() => {

      if(i < text.length){

        message.text += text.charAt(i);
        this.scrollToBottom();
        i++;

      }else{

        clearInterval(interval);

      }

    }, typingSpeed);

  }

}