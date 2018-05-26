package com.xidian.focustime.module;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.R;
import com.xidian.focustime.bean.Greeter;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Executors;

/**
 * Main Ethereum Network
 * https://mainnet.infura.io/[your-token]
 * <p>
 * Test Ethereum Network (Ropsten)
 * https://ropsten.infura.io/[your-token]
 * <p>
 * Test Rinkeby Network
 * https://rinkeby.infura.io/[your-token]
 * <p>
 * IPFS Gateway
 * https://ipfs.infura.io
 * <p>
 * IPFS RPC
 * https://ipfs.infura.io:5001
 */
public class TaskActivity extends AppCompatActivity {


    private final static String privateKeyRopsten = "3d12dae3ebd0406deb87a5f1b937d96ae767648ad1252bec20745729a49e3db5";
    private final static String publicKeyRopsten = "0x48763BE0F3B7875FfA804Ec8B54BB23f08DE7Df6";
    private final static String greeterContractAddressRopsten = "0x3cba4b8b905fb05e626a9663285984c303298f5e";
    
    private final static String ropstenUrl = "https://rinkeby.infura.io/jo3ELHMQjcdXANBPWNoH";
    private TextView token;


    Credentials credentials;
    Web3j web3j;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        path=this.getFilesDir()+"/key.json";
        LogUtils.i("初始化文件"+path);
        try{
            File file = new File(path);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println("{\"address\":\"48763be0f3b7875ffa804ec8b54bb23f08de7df6\",\"id\":\"8d1f3ef2-42e7-4847-9424-06e885d72b9b\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"42d58c725ca4af2bbf71cf222851e9e8fc513c4e7388b03583c63abdf8d8699f\",\"cipherparams\":{\"iv\":\"8819150aa9bba72cb05fafd42e74eed6\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"76073a23948f9564353c8d3258e926dc577ceb246b8d77b68a5fe8cdf1df2a30\"},\"mac\":\"c07bd5dbba451b04d8f2e6b155883a216fe705dae2d9b8907f5ad6ef2b3907d7\"}}");
        }catch (Exception e){
            e.printStackTrace();
        }
        token=findViewById(R.id.token);
        ImageView readButton = findViewById(R.id.read);

        InitTask task = new InitTask();
        task.executeOnExecutor(Executors.newCachedThreadPool());


        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
        ImageView writeButton = findViewById(R.id.add);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToken();
            }
        });



    }



    private void addToken() {
        token.setText("更新中（增加）");
        AddTask addTask = new AddTask();
        addTask.executeOnExecutor(Executors.newCachedThreadPool());
    }


    private void getToken() {
        try {
            token.setText("更新中（读取）");
            QueryTask queryTask = new QueryTask();
            queryTask.executeOnExecutor(Executors.newCachedThreadPool());
        } catch (Exception e) {
            LogUtils.i("wat", "getGreeting exception = " + e.getMessage());
        }
    }

    private class InitTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            try{

                LogUtils.i("读取文件");
                credentials =
                        WalletUtils.loadCredentials(
                                "123456",path);

                web3j = Web3jFactory.build(new HttpService(
                        "https://rinkeby.infura.io/jo3ELHMQjcdXANBPWNoH"));  // FIXME: Enter your Infura token here;

                LogUtils.i("web3");
                LogUtils.i("Credentials loaded");

                String contractAddress = greeterContractAddressRopsten;
                Greeter contract = Greeter.load(contractAddress,web3j, credentials,
                        ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
                //   String contractAddress = contract.getContractAddress();

                LogUtils.i("Smart contract deployed to address " + contractAddress);
                LogUtils.i("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);

                result= contract.balanceOf(credentials.getAddress()).send().toString();


            }catch (Exception e){
                e.printStackTrace();
                result = "Error reading the smart contract. Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            token.setText(result);
        }
    }


    private class QueryTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            try {
                LogUtils.i("查询");
                Greeter greeter = Greeter.load(greeterContractAddressRopsten, web3j, credentials,  ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
                result = greeter.balanceOf(credentials.getAddress()).send().toString();

            } catch (Exception e) {
                result = "Error reading the smart contract. Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            token.setText(result);

        }
    }


    private class AddTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String result;
            try {
                Greeter greeter = Greeter.load(greeterContractAddressRopsten, web3j, credentials,  ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
                TransactionReceipt transactionReceipt = greeter.addcoin().send();
                //   greeter.changeGreeting(new Utf8String(greetingToWrite)).get(3, TimeUnit.MINUTES);
                result = "Successful transaction. Gas used: " + transactionReceipt.getGasUsed();
            } catch (Exception e) {
                result = "Error during transaction. Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            token.setText(result);
        }
    }
}
