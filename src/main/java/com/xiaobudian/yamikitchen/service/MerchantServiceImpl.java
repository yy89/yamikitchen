package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.Favorite;
import com.xiaobudian.yamikitchen.domain.merchant.FavoriteResult;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.FavoriteRepository;
import com.xiaobudian.yamikitchen.repository.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.ProductRepository;
import com.xiaobudian.yamikitchen.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Johnson on 2015/4/24.
 */
@Service(value = "merchantService")
public class MerchantServiceImpl implements MerchantService {
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private FavoriteRepository favoriteRepository;
    @Inject
    private UserRepository userRepository;

    @Override
    public List<Merchant> getMerchants(int page, int pageSize, Double longitude, Double latitude) {
        return merchantRepository.findByLongitudeAndLatitude(longitude, latitude, new PageRequest(page, pageSize));
    }

    @Override
    public Merchant saveMerchant(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Override
    public void removeMerchant(long id) {
        merchantRepository.removeById(id);
        productRepository.removeByMerchantId(id);
    }

    @Override
    public List<Product> getProductsBy(Long rid, Integer page, Integer size) {
        return productRepository.findByMerchantId(rid, new PageRequest(page, size));
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(long id) {
        productRepository.removeById(id);
    }

    @Override
    public FavoriteResult addFavorite(Long rid, Long uid) {
        Favorite favorite = favoriteRepository.findByMerchantIdAndUid(rid, uid);
        if (favorite != null) return null;
        Favorite result = new Favorite(uid, rid);
        User user = userRepository.findOne(uid);
        result.setNickName(user.getNickName());
        result.setHeadPic(user.getHeadPic());
        favoriteRepository.save(result);
        Merchant merchant = merchantRepository.findOne(rid);
        merchant.setFavoriteCount(merchant.getFavoriteCount() + 1);
        merchantRepository.save(merchant);
        return new FavoriteResult(rid, true);
    }

    @Override
    public boolean hasFavorite(Long rid, Long uid) {
        return favoriteRepository.findByMerchantIdAndUid(rid, uid) != null;
    }

    @Override
    public FavoriteResult removeFavorite(Long rid, Long uid) {
        Favorite favorite = favoriteRepository.findByMerchantIdAndUid(rid, uid);
        if (favorite == null) return new FavoriteResult(rid, true);
        favoriteRepository.delete(favorite);
        Merchant merchant = merchantRepository.findOne(rid);
        merchant.setFavoriteCount(merchant.getFavoriteCount() - 1);
        merchantRepository.save(merchant);
        return new FavoriteResult(rid, false);
    }

    @Override
    public List<Merchant> getFavorites(Long uid, Integer page, Integer size) {
        return merchantRepository.findByUidFavorite(uid, new PageRequest(page, size));
    }


    @Override
    public List<Product> gteMainProduct(Long rid) {
        return productRepository.findByMerchantIdAndMainIsTrue(rid);
    }

    @Override
    public Merchant getMerchantBy(Long rid) {
        return merchantRepository.findOne(rid);
    }

    @Override
    public Merchant getMerchantByProductId(long pid) {
        Product product = productRepository.findOne(pid);
        return merchantRepository.findOne(product.getMerchantId());
    }

    @Override
    public Product getProductBy(long pid) {
        return productRepository.findOne(pid);
    }

    @Override
    public int countMerhcantsByCreator(long uid) {
        return merchantRepository.countByCreator(uid);
    }

    @Override
    public Merchant getMerchantByCreator(long creator) {
        return merchantRepository.findByCreator(creator);
    }

    @Override
    public Merchant updateMerchant(Merchant merchant) {
        Merchant merchantdb = merchantRepository.findOne(merchant.getId());
        if(StringUtils.isNotEmpty(merchant.getPictures())){
            merchantdb.setPictures(merchant.getPictures());
        }
        if(StringUtils.isNotEmpty(merchant.getName())){
            merchantdb.setName(merchant.getName());
        }
        if(StringUtils.isNotEmpty(merchant.getHeadPic())){
            merchantdb.setHeadPic(merchant.getHeadPic());
        }
        if(merchant.getType()!=null){
            merchantdb.setType(merchant.getType());
        }
        if(StringUtils.isNotEmpty(merchant.getVoiceIntroduction())){
            merchantdb.setVoiceIntroduction(merchant.getVoiceIntroduction());
        }
        if(StringUtils.isNotEmpty(merchant.getAddress())){
            merchantdb.setAddress(merchant.getAddress());
        }
        if(StringUtils.isNotEmpty(merchant.getPhone())){
            merchantdb.setPhone(merchant.getPhone());
        }
        if(StringUtils.isNotEmpty(merchant.getGoodCuisine())){
            merchantdb.setGoodCuisine(merchant.getGoodCuisine());
        }
        if(StringUtils.isNotEmpty(merchant.getBusinessHours())){
            merchantdb.setBusinessHours(merchant.getBusinessHours());
        }
        if(StringUtils.isNotEmpty(merchant.getBusinessDayPerWeek())){
            merchantdb.setBusinessDayPerWeek(merchant.getBusinessDayPerWeek());
        }
        if(merchant.getSupportDelivery()!=null){
            merchantdb.setSupportDelivery(merchant.getSupportDelivery());
        }
        if(merchant.getDeliverFee()!=null){
            merchantdb.setDeliverFee(merchant.getDeliverFee());
        }
        if(StringUtils.isNotEmpty(merchant.getDeliverComment())){
            merchantdb.setDeliverComment(merchant.getDeliverComment());
        }
        if(merchant.getMessHall()!=null){
            merchantdb.setMessHall(merchant.getMessHall());
        }
        if(merchant.getCountOfMessHall()!=null){
            merchantdb.setCountOfMessHall(merchant.getCountOfMessHall());
        }
        if(merchant.getSelfPickup()!=null){
            merchantdb.setSelfPickup(merchant.getSelfPickup());
        }
        if(StringUtils.isNotEmpty(merchant.getDescription())){
            merchantdb.setDescription(merchant.getDescription());
        }
        merchantRepository.save(merchantdb);
        return merchantdb;
    }

    @Override
    public Product updateProduct(Product product) {
        Product productdb = productRepository.findOne(product.getId());
        if(StringUtils.isNotEmpty(product.getName())){
            productdb.setName(product.getName());
        }
        if(StringUtils.isNotEmpty(product.getPictures())){
            productdb.setPictures(product.getPictures());
        }
        if(product.getPrice()!=null){
            productdb.setPrice(product.getPrice());
        }
        if(StringUtils.isNotEmpty(product.getTags())){
            productdb.setTags(product.getTags());
        }
        if(StringUtils.isNotEmpty(product.getAvailableTime())){
            productdb.setAvailableTime(product.getAvailableTime());
        }
        if(StringUtils.isNotEmpty(product.getSummary())){
            productdb.setSummary(product.getSummary());
        }
        if(product.getSupplyPerDay()!=null){
            productdb.setSupplyPerDay(product.getSupplyPerDay());
        }
        if(product.getAvailable()!=null){
            productdb.setAvailable(product.getAvailable());
        }
        if(product.getMain()!=null){
            productdb.setMain(product.getMain());
        }
        productRepository.save(productdb);
        return productdb;
    }

    @Override
    public Merchant openMerchant(long rid) {
        Merchant merchant = merchantRepository.findOne(rid);
        merchant.setIsRest(false);
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant closeMerchant(long rid) {
        Merchant merchant = merchantRepository.findOne(rid);
        merchant.setIsRest(true);
        return merchantRepository.save(merchant);
    }

    @Override
    public Product putOnProduct(long pid) {
        Product product = productRepository.findOne(pid);
        product.setAvailable(true);
        return productRepository.save(product);
    }

    @Override
    public Product putOffProduct(long pid) {
        Product product = productRepository.findOne(pid);
        product.setAvailable(false);
        return productRepository.save(product);
    }
}
