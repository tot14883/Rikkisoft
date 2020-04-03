package com.rikkeisoft.rikkonbi.Presenter;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel.PiSignage;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;

import org.json.JSONException;

import java.util.List;

public interface MainActivityPresenter {
   interface Model{
      //PrefereneceCart
       interface CartPre{
              void PreferenceCartModel(OnSuccessDataCart onSuccessData,List<String> myCart,Context context);
              void PreferenceCartDuplicateModel(OnSuccessDataCart onSuccessData,String myCart,Context context);
          interface OnSuccessDataCart {
                  void OnSuccessPreferenceCart(List<String> myCart, Context context);
                  void OnSuccessPreferenceCartDuplicate(String myCart, Context context);
          }
       }

      //Login
      interface InfoMember{
          void getInfoMember(OnSuccessDataMember onSuccessData,String tokenID);
          interface OnSuccessDataMember{
              void onSuccessMember(List<String> userArrayList);
              void OnFailureMember(String check);
              void onFailure(Throwable t);
          }
      }

      //ProductDetail
       interface ProductDetail{
          void getInfoProductDetail(OnSuccessDataProduct onSuccessData,String tokenUser,int id);
          interface OnSuccessDataProduct{
              void onSuccessProductDetail(List<String> productDetailList);
              void onFailure(Throwable t);
          }
      }

      //Asset Image
       interface InfoAsset{
          void getInfoAsset(OnSuccessDataAsset onSuccessData,String tokenUser);
          interface OnSuccessDataAsset{
              void onSuccessAsset(List<PiSignage> assetArrayList);
              void onFailure(Throwable t);
          }
      }


      interface ProductItem{
          void getInfoProduct(OnSuccessDataItemProduct onSuccessData,String tokenUser);
          //ProductItem
          interface OnSuccessDataItemProduct{
              void onSuccessProduct(List<Product> productArrayList);
              void onFailure(Throwable t);
          }
      }

      //PostOrder
       interface PostOrder{
          void postInfoOrder(OnSuccessPostOrder onSuccessData,List<String> orderItems,String tokenUser,int len) throws JSONException;
          interface OnSuccessPostOrder{
              void onSuccessPostOrder(String status);
              void onFailOrder(int fail,int len);
          }
      }

      //GetOrder
       interface GetOrder{
           void getInfoOrder(OnSuccessGetOrder onSuccessGetOrder,String tokenUser,String userId);
           interface OnSuccessGetOrder{
               void OnSuccessGetOrder(List<String> historyItems);
               void onFailOrderHistory(String fail);
           }
      }

      interface deleteHistory{
           void deleteHistory(OnSuccessDeleteOrder onSuccessDeleteOrder,String tokenUser,int id, String userId);
           interface OnSuccessDeleteOrder{
               void OnSuccessDelete(String status,String tokenUser, String userId);
               void OnFailOrderDelete(String status,String tokenId,String userId,int productId);
           }
      }

      interface MyWallets{
          void getInfoMyWallets(OnSuccessWallets onSuccessWallets,String tokenUser);
          interface OnSuccessWallets{
              void OnShowMyWallets(String money);
              void OnFailShowMyWallets(String status);
          }
      }

      interface getNotification{
           void getNotificationInfo(OnSuccessNotification onSuccessNotification,String tokenUser,int pageNum);
           interface OnSuccessNotification{
               void OnShowNotification(List<String> message,int pageIndex);
               void OnFailNotification(String status);
               void OnRefreshData(int pageIndex);
           }
      }

      interface settingNotification{
           void postDelNotification(OnSuccessPostNotification onSuccessPostNotification, String token, int id);
           void postReadNotification(OnSuccessPostNotification onSuccessPostNotification,String token, int id);
           interface OnSuccessPostNotification{
               void OnDelSuccessNotifi(String status);
               void OnReadSuccessNotifi(String status);
               void OnFailPostNotification(String status);
           }
      }

      interface PostFCM{
           void postNotificationFCM(OnSuccessPostNotificationFCM onSuccessPostNotificationFCM,String fcmToken,String token);
           void delNotificationFCM(OnSuccessPostNotificationFCM onSuccessPostNotificationFCM,String fcmToken,String token);
           interface OnSuccessPostNotificationFCM{
               void OnSuccessFCM(String status);
               void OnFailFCM(String status);
           }
      }

      interface GetContact{
           void getContact(OnSuccessGetContact onSuccessGetContact,String token);
           interface OnSuccessGetContact{
               void OnSuccessContact(String content,String hotline);
               void OnFailContact(String status);
           }
      }

   }
   interface View{

       interface LoginView{
           void UserAccount(List<String> user);
           void SignOut(String signout);
           void showProgress(String message);
           void ChangePage(boolean val);
           void showMessage(String message);
       }

       interface AccountView{
       }

       interface HomeView{
           void setDataToImageSlide(List<PiSignage> assetArrayList);
           void ShowView(String view);
       }

       interface MainView{
           void SignOut(String signout);
       }

       interface CategoryView{
           void OnClickItem(int position);
           void setDataToRecycleView(List<Product> productArrayList);
       }

       interface CategoryDetailView{
           void showProgress(String message);
           void showMessage(String message);
           void OnClickItem(int position);
           //void OrderQuantity(String command,int checkProduct);
       }

       interface CartView{
          // String CheckOverProduct();
           void setDataToRecycleOrder(List<String> carts);
           void OnClickItem(int position);
          // void OrderQuantity(String command,int checkProduct);
           void showMessage(String message);
           void showProgress(String message);
           void checkQuantity(String cartQuantity);
       }

       interface HistoryView{
           void setDataToHistory(List<String> history);
           void showProgress(String message);
           void showMessage(String message);
           void OnClickItem(String position);
       }

       interface ConnectedWifi{
           void showConnectedWifi(boolean isConnected);
       }

       interface MessageNotificationView{
           void setDataNotification(List<String> notification);
           void showProgress(String message);
           void showMessage(String message);
           void OnClickItem(String position);
           void RefreshRecycle(int pageIndex);
       }

       interface NotificationSettingView{
           void Progress(String status);
       }

       interface ContactView{
           void ShowMessage(String contant, String hotline);
       }

   }
   interface Presenter{

     //LoginBackground
     interface LoginBackGround{
         void LoginBackground(GoogleSignInAccount account, Boolean check);
         void UserBackground(String account);
     }

     //MainPresenter
     interface MainBackground{
         void UserBackground(String account);
     }

     //HomePresenter
     interface HomeBackground{
         void HomeBackground(String tokenUser);
     }

     //CategoryBackground
     interface CategoryBackground{
         void ProductBackground(String tokenUser);
     }

     //DetailCategoryBackground
     interface DetailCategoryBackground{
         void PreferenceCart(List<String> myCart,Context context);
         void ProductDetailBackground(String tokenUser,int Id);
     }

     //AccountBackground
     interface AccountBackground{

     }

     //CartBackground
     interface CartBackground{
         void PreferenceCart(String myCart,Context context);
         void ProductDetailBackground(String tokenUser,int Id);
     }

     interface PostOrderBackground{
         void PostOrder(List<String> cart,String tokenUser,int len);
     }

     interface HistoryBackground{
        void HistoryBackground(String tokenUser,String userId);
        void HistoryDelete(String tokenUser,int id,String userId,int position);
       // void GetHistory(String tokenUser,)
     }

     interface CheckWiFiBackground{
         void CheckWiFiBackground();
     }

     interface NotificationBackground{
         void NotificationBackground(String tokenUser,int pageNum);
     }

     interface SettingNotifiBackground{
         void NotificationDel(String tokenUser, int id);
         void NotificationRead(String tokenUser,int id);
     }

     interface NotificationFCM{
         void NotificationFCM(String tokenUser,String fcmToken);
         void DelNotificationFCM(String tokenUser,String fcmToken);
     }

     interface ContactBackground{
         void ContactBackground(String tokenUser);
     }

   }
}
