import { Button, Container, Grid, TextField, Typography } from "@mui/material";
import React from "react";
import { KAKAO_AUTH_URL } from "../../config/kakao-config";
import { NAVER_AUTH_URL } from "../../config/naver-config";

const Login = () => {
  return (
    <>
      {true && (
        <Container
          component="main"
          maxWidth="xs"
          style={{ margin: "200px auto" }}
        >
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Typography component="h1" variant="h5">
                로그인
              </Typography>
            </Grid>
          </Grid>

          <form
            noValidate
            //onSubmit={loginHandler}
          >
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  id="email"
                  label="email address"
                  name="email"
                  autoComplete="email"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  name="password"
                  label="on your password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                />
              </Grid>
              <Grid item xs={12}>
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="primary"
                >
                  로그인
                </Button>
              </Grid>
              <Grid item xs={12}>
                <a href={KAKAO_AUTH_URL}>
                  <img
                    style={{ width: "100%" }}
                    src={require("../../assets/img/kakao_login_medium_wide.png")}
                    alt="카카오 로그인"
                  />
                </a>
              </Grid>
              <Grid item xs={12}>
                <a href={NAVER_AUTH_URL}>
                  <img
                    style={{ width: "100%" }}
                    src={require("../../assets/img/naver_login.png")}
                    alt="네이버 로그인"
                  />
                </a>
              </Grid>
            </Grid>
          </form>
        </Container>
      )}
      {/* <CustomSnackBar open={isLoggedIn} /> */}
    </>
  );
};

export default Login;
