package com.arrow.pegasus.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;

public class CoreUserDetails extends Loggable implements UserDetails {
	private static final long serialVersionUID = 702759906442465826L;

	private final User user;
	private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	private User impersonateUser;

	public CoreUserDetails(User user) {
		String method = "CoreUserDetails";
		this.user = user;
		user.getRefRoles().forEach(role -> {
			role.getRefPrivileges().forEach(priv -> {
				logDebug(method, "processing authority: %s - %s - %s - %s - %s", priv.getSystemName(), priv.getId(),
				        role.getName(), role.getRefApplication().getName(), role.getId());
				authorities.add(new SimpleGrantedAuthority(priv.getSystemName()));
				logDebug(method, "added authority: %s - %s - %s - %s - %s", priv.getSystemName(), priv.getId(),
				        role.getName(), role.getRefApplication().getName(), role.getId());
			});
		});
		clearSensitiveInfo(this.user);
	}

	public User getUser() {
		return user;
	}

	public User getImpersonateUser() {
		return impersonateUser;
	}

	public void setImpersonateUser(User impersonateUser) {
		this.impersonateUser = impersonateUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableList(authorities);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getStatus() == UserStatus.Active;
	}

	private void clearSensitiveInfo(User user) {
		if (user != null) {
			user.setSalt(null);
			user.setPassword(null);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoreUserDetails other = (CoreUserDetails) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}