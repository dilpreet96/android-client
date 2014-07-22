/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package com.mifos.mifosxdroid.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.mifos.mifosxdroid.R;
import com.mifos.objects.db.Loan;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ishankhanna on 21/07/14.
 */
public class CollectionSheetLoanAccountListAdapter extends BaseAdapter {

    private static final String TAG = "LoanAccountListAdapter";
    LayoutInflater layoutInflater;
    List<Loan> loans = new ArrayList<Loan>();
    int groupPosition;
    int childPosition;

    int positionBeingEdited = -1;

    public CollectionSheetLoanAccountListAdapter(Context context, List<Loan> loans, int groupPosition, int childPosition) {

        layoutInflater = LayoutInflater.from(context);
        this.loans = loans;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
    }

    @Override
    public int getCount() {
        return loans.size();
    }

    @Override
    public Loan getItem(int position) {
        return loans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ReusableViewHolder reusableViewHolder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_collection_sheet_loan, null);
            reusableViewHolder = new ReusableViewHolder(convertView);
            convertView.setTag(reusableViewHolder);

        } else {
            reusableViewHolder = (ReusableViewHolder) convertView.getTag();
        }

        Log.i(TAG, "Loan Id = "+loans.get(position).getId() +"\n"+" Loan Total Due ="+ loans.get(position).getTotalDue()+"\n"+"Repayment Amount ="
                + CollectionListAdapter.sRepaymentTransactions.get(loans.get(position).getLoanId()));

        Double transactionAmount = CollectionListAdapter.sRepaymentTransactions.get(loans.get(position).getLoanId());

        reusableViewHolder.tv_amountDue.setText(String.valueOf(loans.get(position).getTotalDue()));
        reusableViewHolder.tv_loanShortName.setText(loans.get(position).getProductShortName());
        reusableViewHolder.et_amountPaid.setText(String.valueOf(transactionAmount));

        reusableViewHolder.et_amountPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                CollectionListAdapter.sRepaymentTransactions.put(loans.get(position).getLoanId(), Double.parseDouble(s.toString()));

                /* TODO Fix Live update of Amounts
                CollectionListAdapter.sMifosGroups.get(groupPosition).getClients().get(childPosition).getLoans().get(position).setTotalDue(Double.parseDouble(s.toString()));
                CollectionSheetFragment.refreshFragment();
                reusableViewHolder.et_amountPaid.requestFocus();
                */
            }
        });

        return convertView;
    }

    public static class ReusableViewHolder {

        @InjectView(R.id.tv_loan_shortname)
        TextView tv_loanShortName;
        @InjectView(R.id.tv_amountDue)
        TextView tv_amountDue;
        @InjectView(R.id.et_amountPaid)
        EditText et_amountPaid;

        public ReusableViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}