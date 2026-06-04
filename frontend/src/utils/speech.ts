// 语音合成工具 - 使用浏览器 Web Speech API（免费）

export interface SpeechOptions {
  text: string;
  voiceType?: 'male' | 'female';
  lang?: string;
  rate?: number;
  pitch?: number;
  volume?: number;
  onStart?: () => void;
  onEnd?: () => void;
  onError?: (error: any) => void;
}

// 检查浏览器是否支持语音合成
export const isSpeechSupported = (): boolean => {
  return 'speechSynthesis' in window;
};

// 获取可用的语音列表
export const getAvailableVoices = (): SpeechSynthesisVoice[] => {
  return window.speechSynthesis.getVoices();
};

// 按类型获取语音
export const getVoiceByType = (type: 'male' | 'female', lang: string = 'zh-CN'): SpeechSynthesisVoice | null => {
  const voices = getAvailableVoices();
  const filteredVoices = voices.filter(v => v.lang.startsWith(lang));

  if (filteredVoices.length === 0) {
    // 如果没有找到指定语言的语音，返回第一个可用的
    return voices[0] || null;
  }

  // 简单的性别判断（根据语音名称）
  const maleKeywords = ['male', '男', 'David', 'Paul', 'Daniel', 'Alex', 'Fred'];
  const femaleKeywords = ['female', '女', 'Victoria', 'Kate', 'Samantha', 'Anna', 'Karen'];

  if (type === 'male') {
    const maleVoice = filteredVoices.find(v =>
      maleKeywords.some(keyword => v.name.toLowerCase().includes(keyword.toLowerCase()))
    );
    return maleVoice || filteredVoices[0];
  } else {
    const femaleVoice = filteredVoices.find(v =>
      femaleKeywords.some(keyword => v.name.toLowerCase().includes(keyword.toLowerCase()))
    );
    return femaleVoice || filteredVoices[0];
  }
};

// 播放语音
export const speak = (options: SpeechOptions): SpeechSynthesisUtterance | null => {
  if (!isSpeechSupported()) {
    console.error('浏览器不支持语音合成');
    options.onError?.('浏览器不支持语音合成');
    return null;
  }

  // 停止之前的语音
  window.speechSynthesis.cancel();

  const utterance = new SpeechSynthesisUtterance(options.text);

  // 设置语音参数
  const voice = getVoiceByType(options.voiceType || 'female', options.lang || 'zh-CN');
  if (voice) {
    utterance.voice = voice;
  }

  utterance.lang = options.lang || 'zh-CN';
  utterance.rate = options.rate || 1.0; // 语速 0.1 - 10
  utterance.pitch = options.pitch || 1.0; // 音调 0 - 2
  utterance.volume = options.volume || 1.0; // 音量 0 - 1

  // 事件回调
  utterance.onstart = () => options.onStart?.();
  utterance.onend = () => options.onEnd?.();
  utterance.onerror = (e) => options.onError?.(e);

  window.speechSynthesis.speak(utterance);

  return utterance;
};

// 停止语音
export const stopSpeaking = (): void => {
  if (isSpeechSupported()) {
    window.speechSynthesis.cancel();
  }
};

// 暂停语音
export const pauseSpeaking = (): void => {
  if (isSpeechSupported()) {
    window.speechSynthesis.pause();
  }
};

// 恢复语音
export const resumeSpeaking = (): void => {
  if (isSpeechSupported()) {
    window.speechSynthesis.resume();
  }
};

// 检查是否正在播放
export const isSpeaking = (): boolean => {
  return isSpeechSupported() && window.speechSynthesis.speaking;
};

// 别名导出，方便使用
export const stop = stopSpeaking;
export const getVoices = getAvailableVoices;
