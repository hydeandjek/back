import { Route, Routes } from "react-router-dom";

//import KakaoMap from './Components/KakaoMap/KakaoMap';
import Login from "./Components/User/Login";
import Home from "./components/home/Home";
import KakaoLoginHandler from "./components/user/KakaoLOginHandler";
import NaverLoginHandler from "./components/user/NaverLoginHandler";

function App() {
  // return (
  //   <div className='App'>
  //     <KakaoMap
  //       searchWord={'약국'}
  //       address={'서울특별시 마포구 백범로 23'}
  //     />
  //   </div>
  // );
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/oauth/redirected/kakao" element={<KakaoLoginHandler />} />
        <Route path="/oauth/redirected/naver" element={<NaverLoginHandler />} />
      </Routes>
    </div>
  );
}

export default App;
