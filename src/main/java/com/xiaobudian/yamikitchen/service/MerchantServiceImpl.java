package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.Favorite;
import com.xiaobudian.yamikitchen.domain.merchant.FavoriteResult;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import com.xiaobudian.yamikitchen.repository.merchant.FavoriteRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private AccountRepository accountRepository;

    @Override
    public List<Merchant> getMerchants(int page, int pageSize, Double longitude, Double latitude) {
        return merchantRepository.findByLongitudeAndLatitude(longitude, latitude, new PageRequest(page, pageSize));
    }

    @Override
    public Merchant saveMerchant(Merchant merchant) {
        Long merchantNo = redisRepository.nextLong(Keys.nextMerchantNoKey());
        if (StringUtils.isEmpty(merchant.getMerchantNo())) merchant.setMerchantNo(String.valueOf(merchantNo));
        Merchant result = merchantRepository.save(merchant);
        accountRepository.save(result.createAccounts());
        return result;
    }

    @Override
    public boolean removeMerchant(long id) {
        merchantRepository.removeById(id);
        productRepository.removeByMerchantId(id);
        return true;
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
    public boolean removeProduct(long id) {
        productRepository.removeById(id);
        return true;
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
    public List<Product> getMainProducts(Long rid) {
        return productRepository.findMainProducts(rid);
    }

    @Override
    public Merchant getMerchantBy(Long rid) {
        return merchantRepository.findOne(rid);
    }


    @Override
    public Product getProductBy(Long pid) {
        return productRepository.findOne(pid);
    }

    @Override
    public Merchant getMerchantByCreator(Long creator) {
        return merchantRepository.findByCreator(creator);
    }

    @Override
    public Merchant updateMerchant(Merchant merchant) {
        Merchant m = merchantRepository.findOne(merchant.getId());
        BeanUtils.copyProperties(merchant, m, Util.getNullPropertyNames(merchant));
        return merchantRepository.save(m);
    }

    @Override
    public Product updateProduct(Product product) {
        Product p = productRepository.findOne(product.getId());
        BeanUtils.copyProperties(product, p, Util.getNullPropertyNames(product));
        return productRepository.save(p);
    }

    @Override
    public Merchant changeMerchantRestStatus(Long rid, boolean isRest) {
        Merchant merchant = merchantRepository.findOne(rid);
        merchant.setIsRest(isRest);
        return merchantRepository.save(merchant);
    }

    @Override
    public Product changeProductAvailability(Long pid, boolean available) {
        Product product = productRepository.findOne(pid);
        product.setAvailable(available);
        return productRepository.save(product);
    }

    @Override
    public Product changeProductMain(Long pid, boolean isMain) {
        Product product = productRepository.findOne(pid);
        if (isMain) productRepository.disableMain(product.getMerchantId());
        product.setMain(isMain);
        return productRepository.save(product);
    }
}
