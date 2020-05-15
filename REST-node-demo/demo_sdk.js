var config = require('./config/default');
const bgsdk = require('./sdk/bgsdk');

function run() {

    bgsdk.accounts(config.bitget.access_key).then(console.log);
    //bgsdk.balance(config.bitget.access_key,'390350274889256960').then(console.log);
    //accesskey,account_id,amount,price,symbol,type
    //bgsdk.place(config.bitget.access_key,'390350274889256960','10','0.003','eth_btc','buy-limit');
    //bgsdk.submitcancel(config.bitget.access_key,'406368151517835264');
    // bgsdk.order(config.bitget.access_key,'406368151517835264');
    // bgsdk.matchresults(config.bitget.access_key,'406368151517835264');
    //symbol,types,start_date,end_date,states,size,fromId,direct)
    // bgsdk.matchresultsHistory(config.bitget.access_key,'eth_btc','sell-market','2018-06-01','2018-07-20','submitted','50','','');
    // bgsdk.orders(config.bitget.access_key,'eth_btc','sell-market','2018-06-01','2018-07-20','submitted','50','','');
    //addressWithdraw,amount,currency,fees
    // bgsdk.withdrawCreate(config.bitget.access_key,'1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi','10','btc');
    // bgsdk.withdrawCancel(config.bitget.access_key,'436');

    //bgsdk.withdrawSelect(config.bitget.access_key,'btc','withdraw','10');
    bgsdk.batchcancel(config.bitget.access_key,'[1,2,3,4]');




    // bgsdk.kline('iost_btc','1min','1').then(console.log);
    // bgsdk.merged('iost_btc').then(console.log);
    // bgsdk.ticckers().then(console.log);
    // bgsdk.depth('iost_btc','step1').then(console.log);
    // bgsdk.trade('iost_btc').then(console.log);
    // bgsdk.trades('iost_btc','10').then(console.log);
    // bgsdk.detail('iost_btc').then(console.log);
    // bgsdk.symbols().then(console.log);
    // bgsdk.currencys().then(console.log);
    // bgsdk.timestamp().then(console.log);


}

run();